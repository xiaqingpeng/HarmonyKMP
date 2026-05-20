# 鸿蒙与 iOS 运行问题排查

## 1. 适用范围

本文主要记录本项目近期排查过的两类运行问题：

1. 鸿蒙端修改共享代码后，页面没有同步变化，表现为“Android 刷新了，鸿蒙没刷新”。
2. iOS 真机运行时出现签名、安装、启动失败，例如：
   - 无法创建 provisioning profile
   - 免费开发者账号安装数量已达上限
   - Xcode 仍然尝试启动旧的 Bundle Identifier

本文只针对当前工程结构：

- `composeApp/`：KMP 共享模块
- `harmonyApp/`：鸿蒙宿主
- `iosApp/`：iOS 宿主

---

## 2. 鸿蒙端修改代码后页面不刷新

### 2.1 现象

常见表现：

- 修改 `commonMain` 的 UI 后，Android 运行结果已变化。
- 鸿蒙端重新打开页面后，还是旧页面。
- 看起来像“Compose 没重组”或者“页面没有刷新”。

### 2.2 根因

这个问题在本项目里通常不是 Compose 重组失败，而是下面两类原因之一：

#### 原因一：鸿蒙入口和 Android 入口不是同一套页面树

Android 原本直接走：

- `composeApp/src/androidMain/kotlin/com/example/cmp_hello/MainActivity.kt`
- `setContent { App() }`

鸿蒙原本走的是：

- `composeApp/src/ohosMain/kotlin/com/example/cmp_hello/MainArkUIViewController.kt`
- `composeApp/src/ohosArm64Main/kotlin/com/example/cmp_hello/OhosRootApp.ohosArm64.kt`

也就是说：

- Android 改的是 `App()`
- 鸿蒙实际显示的是 `OhosRootApp()`

如果两个入口不是同一套顶层 UI，那么你改共享页面时，很容易出现 Android 有变化、鸿蒙没变化。

#### 原因二：鸿蒙运行的不是源码，而是打包进 HAP 的 `libkn.so`

鸿蒙这套链路不是直接解释执行 Kotlin 页面，而是：

1. `composeApp` 编译出 `libkn.so`
2. 通过 `publishDebugBinariesToHarmonyApp` 复制到 `harmonyApp`
3. DevEco 再将其打包进 HAP
4. 设备运行时由 ArkTS + NAPI 加载该动态库

因此，改完 Kotlin / Compose 代码后，如果没有重新发布并重新运行鸿蒙应用，设备上还是旧的 `libkn.so`。

### 2.3 解决方案

#### 方案一：统一鸿蒙与共享入口

建议让鸿蒙顶层入口也复用共享 `App()`。

本次处理方式：

- `composeApp/src/ohosArm64Main/kotlin/com/example/cmp_hello/OhosRootApp.ohosArm64.kt`

调整为：

```kotlin
@Composable
internal actual fun OhosRootApp() {
    App()
}
```

这样可以保证：

- Android、iOS、OHOS 顶层入口尽量一致
- 修改共享页面时，三端更容易保持同步

#### 方案二：每次修改后重新发布鸿蒙产物

执行：

```bash
./gradlew :composeApp:publishDebugBinariesToHarmonyApp
```

作用：

- 重新编译 `libkn.so`
- 更新 `libkn_api.h`
- 同步资源到 `harmonyApp`

然后在 DevEco Studio 中重新运行鸿蒙应用。

注意：

- 这个动作不是“热刷新”
- 它本质上是重新生成原生产物并重新打包安装

### 2.4 排查顺序

建议按这个顺序查：

1. 先确认鸿蒙实际入口是不是 `App()`。
2. 再确认是否已经执行过 `publishDebugBinariesToHarmonyApp`。
3. 再确认 DevEco 是否重新安装了最新 HAP。
4. 最后才去怀疑 Compose 状态或重组本身。

### 2.5 验证方法

最简单的验证方式：

1. 修改共享页面里一段非常明显的文案。
2. 执行：

```bash
./gradlew :composeApp:publishDebugBinariesToHarmonyApp
```

3. 重新在 DevEco 运行 `harmonyApp`
4. 观察鸿蒙页面是否同步显示新文案

---

## 3. iOS 自动签名失败：Provisioning Profile 创建失败

### 3.1 现象

Xcode 提示类似错误：

- `Xcode couldn't find any iOS App Development provisioning profiles matching ...`
- `Create iOS App Development provisioning profile for bundle identifier ...`
- `The attribute 'name' is invalid`

本次实际错误是：

```text
The attribute 'name' is invalid: 'XC com example cmp_hello cmp_hello'
```

### 3.2 根因

本项目原来的 Bundle Identifier 为：

```text
com.example.cmp_hello.cmp_hello
```

Xcode 自动签名时，会在 Apple 后台尝试创建对应 App ID。该过程中生成的名字里带有下划线，导致 Apple 后台拒绝创建。

### 3.3 解决方案

将：

- `iosApp/Configuration/Config.xcconfig`

中的：

```text
PRODUCT_BUNDLE_IDENTIFIER=com.example.cmp_hello.cmp_hello$(TEAM_ID)
```

改为：

```text
PRODUCT_BUNDLE_IDENTIFIER=com.example.cmphello.cmphello$(TEAM_ID)
```

建议：

- iOS Bundle Identifier 尽量只使用字母、数字和点
- 避免使用下划线

### 3.4 验证方式

重新打开 Xcode，重新执行自动签名后，若不再出现 `The attribute 'name' is invalid`，说明问题已修复。

---

## 4. iOS 真机安装失败：免费开发者账号安装数量达到上限

### 4.1 现象

Xcode 提示类似：

```text
This device has reached the maximum number of installed apps using a free developer profile
```

并伴随：

- `无法安装此 App，因为无法验证其完整性`
- `MIInstallerErrorDomain Code: 13`

### 4.2 根因

这是 Apple 免费个人开发者账号的限制，不是项目代码错误。

当同一台真机上，已经安装了多个使用免费开发者证书签名的 App 时，再装新的 App 会失败。

### 4.3 解决方案

在真机上卸载一个或多个旧的测试 App，然后重新运行当前项目。

推荐步骤：

1. 删除手机上旧的测试包
2. 回到 Xcode 点击 Run
3. 若仍失败，再继续删除其他旧包后重试

### 4.4 可替代方案

如果不想删除旧应用，可以改为：

- 使用 iOS 模拟器
- 使用付费 Apple Developer Program 账号
- 更换一台未达到上限的真机

---

## 5. iOS 启动失败：Xcode 仍然使用旧 Bundle Identifier

### 5.1 现象

即使 Bundle Identifier 已经改成新的值，Xcode 仍提示：

```text
The requested application com.example.cmp_hello.cmp_hello is not installed.
```

也就是：

- 编译产物已经是新的 Bundle ID
- 但 Xcode 在启动阶段还在找旧的 Bundle ID

### 5.2 根因

这类问题通常不是源码配置错误，而是 Xcode 的运行缓存、用户态 scheme 缓存、DerivedData 或上次失败的启动状态没有刷新。

本次排查已经确认：

1. 工程配置中的 Bundle ID 是新的
2. 编译产物 `.app` 中的 `CFBundleIdentifier` 也是新的
3. 只有启动阶段仍然拿着旧 ID 去找设备上的 App

因此问题在于 Xcode 本地缓存，而不是代码本身。

### 5.3 解决方案

建议按顺序执行：

1. 停止 Xcode 当前运行
2. `Product > Clean Build Folder`
3. 删除真机上已有的旧测试图标
4. 关闭 Xcode
5. 删除 DerivedData 中对应工程目录
6. 删除工程下的 `xcuserdata` 相关缓存
7. 重新打开 Xcode，再运行一次

建议重点清理：

- `iosApp/iosApp.xcodeproj/xcuserdata`
- `iosApp/iosApp.xcodeproj/project.xcworkspace/xcuserdata`
- `~/Library/Developer/Xcode/DerivedData/...`

### 5.4 验证方式

可以直接检查编译产物中的实际 Bundle Identifier 是否正确。

若 `.app/Info.plist` 中已经是新的 Bundle ID，而 Xcode 仍然去找旧的 Bundle ID，那么基本就能确认是启动缓存问题。

---

## 6. 建议的运行流程

### 6.1 鸿蒙

修改共享代码后，建议固定按下面流程执行：

1. 修改 `composeApp` 中代码
2. 执行：

```bash
./gradlew :composeApp:publishDebugBinariesToHarmonyApp
```

3. 在 DevEco Studio 重新运行 `harmonyApp`

### 6.2 iOS

修改 iOS 配置或签名相关内容后，建议固定按下面流程执行：

1. 确认 `Config.xcconfig` 中 Bundle Identifier 正确
2. 确认 Xcode 选中了正确的 Team
3. 如果遇到旧 ID 启动失败，先清理：
   - Clean Build Folder
   - DerivedData
   - `xcuserdata`
4. 再重新运行真机

---

## 7. 总结

这两类问题的核心区别是：

- 鸿蒙侧更多是“入口不一致”和“动态库产物未重新发布”的问题
- iOS 侧更多是“签名规则限制”和“Xcode 本地运行缓存”的问题

实际排查时建议遵循下面思路：

1. 先确认入口是否一致
2. 再确认构建产物是否真正更新
3. 再确认安装 / 签名 / 启动缓存是否干净
4. 最后才去怀疑页面状态、Compose 逻辑或业务代码本身

按这个顺序查，通常会比一上来怀疑“页面没刷新”“Compose 坏了”“真机抽风了”更快定位问题。
