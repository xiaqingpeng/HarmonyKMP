# OHOS Navigation `SavedStateHandle` 回传验证

## 1. 目标

这个 Demo 用来验证当前仓库的 `ohosArm64` 构建里，`navigation-compose` 的页面回传链路是否正常：

- 子页通过 `previousBackStackEntry.savedStateHandle.set(...)` 把结果写回父页。
- 父页通过 `currentBackStackEntry.savedStateHandle.getStateFlow(...)` 读取结果并刷新 UI。
- 通过日志同时对比 `raw`、`hasKey` 和界面显示值，区分“实际值是否改变”和“UI 是否刷新”。

## 2. 第三方库与使用方法

本 Demo 主要依赖以下第三方库：

- `org.jetbrains.androidx.navigation:navigation-compose`
- 其传递依赖会带上 `navigation-common`、`navigation-runtime`、`lifecycle-viewmodel-savedstate`、`savedstate` 等 OHOS 版本组件

在本仓库中，依赖只放在 `ohosMain`，不涉及 Android / iOS：

```kotlin
ohosMain.dependencies {
    api(libs.compose.multiplatform.export)
    implementation(libs.androidx.navigation.compose)
}
```

版本定义在 `gradle/libs.versions.toml`，当前使用的是与仓库 OHOS 栈匹配的 `2.9.4-0.2.1-03`。

### 2.1 路由与控制器

核心 API 的用法如下：

- `rememberNavController()`：创建导航控制器
- `NavHost(...)`：声明导航图
- `composable(route) { ... }`：声明页面
- `currentBackStackEntryAsState()`：监听当前页的 back stack entry
- `previousBackStackEntry`：获取上一页的 entry，用于回传

### 2.2 回传数据

父页读取：

- `savedStateHandle.getStateFlow(ResultKey, DefaultResult)`
- 再通过 `collectAsState()` 绑定到 Compose UI

子页回传：

- `navController.previousBackStackEntry?.savedStateHandle?.set(ResultKey, value)`
- 再 `popBackStack()` 返回父页

清空结果：

- `savedStateHandle.set(ResultKey, DefaultResult)`
- 这个操作会立即把父页的当前结果恢复到默认值，适合验证“界面是否马上刷新”
- 如果你要验证“真正删除 key”的语义，可以额外再加一个 `remove<String>(ResultKey)` 按钮；那是另一种测试场景

## 3. Demo 结构

相关文件：

- `composeApp/src/ohosMain/kotlin/com/example/cmp_hello/sample/navigation/OhosNavigationSavedStateHandleDemo.kt`
- `composeApp/src/ohosArm64Main/kotlin/com/example/cmp_hello/sample/mainpage/OhosDisplaySections.ohosArm64.kt`

首页新增了一个 `Navigation` 分组，点击 `SavedStateHandle 回传验证` 即可进入 Demo。

页面流程：

1. 首页显示默认文案 `尚未收到子页回传`
2. 点击 `打开子页`
3. 在子页修改文本
4. 点击 `写回并返回`
5. 父页显示新值
6. 点击 `清空结果`
7. 通过日志确认 `SavedStateHandle` 的 key 是否真的被删除

## 4. 测试说明

### 4.1 构建与发布

在项目根目录执行：

```bash
./gradlew :composeApp:publishDebugBinariesToHarmonyApp
```

然后在 DevEco Studio 中打开 `harmonyApp`，同步并运行 `debug` 版本。

### 4.2 测试步骤

1. 启动 OHOS ARM64 真机或模拟器。
2. 打开首页，进入 `Navigation > SavedStateHandle 回传验证`。
3. 观察日志中是否出现：
   - `CMP_NAV: demo start`
   - `CMP_NAV: home observed result=尚未收到子页回传 ...`
4. 点击 `打开子页`，在子页修改文本后点击 `写回并返回`。
5. 返回父页后观察是否出现：
   - `child submit before ...`
   - `child submit after ...`
   - `home observed result=你的新值 ...`
6. 点击 `恢复默认`，观察是否出现：
   - `home reset before ... raw=你的新值 hasKey=true`
   - `home reset after ... raw=尚未收到子页回传 hasKey=true`

### 4.3 日志判读

建议重点看 `CMP_NAV` 日志的这三个字段：

- `uiResult` / `result`：界面当前显示的值
- `raw`：`SavedStateHandle` 里直接读取的值
- `hasKey`：这个 key 是否还存在

判读规则：

- 如果 `raw=尚未收到子页回传` 且 `hasKey=true`，说明 UI 已经恢复默认值
- 如果你额外加了“删除 key”按钮，那么 `raw=null` 且 `hasKey=false` 才表示 key 真正被删除
- 如果 `child submit after` 已经是新值，说明子页回传链路是通的

## 5. 已知现象

当前 Demo 的 `恢复默认` 按钮是“把 UI 重置为默认值”语义，不是“删除 key”语义。

如果你想让界面也立即恢复默认值，应该把该 key 重新写回默认值：

```kotlin
currentEntry?.savedStateHandle?.set(ResultKey, DefaultResult)
```

如果你想同时验证删除语义，建议再单独加一个 `remove<String>(ResultKey)` 按钮，不要和“恢复默认”混在一起。

## 6. 官方文档链接

- JetBrains Compose Multiplatform + Jetpack Compose: https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-and-jetpack-compose.html
- JetBrains Compose Navigation: https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation-routing.html
- Android `SavedStateHandle` API: https://developer.android.com/reference/kotlin/androidx/lifecycle/SavedStateHandle
- Android Navigation programmatic results: https://developer.android.com/guide/navigation/use-graph/programmatic

## 7. 相关文件

- `composeApp/build.gradle.kts`
- `gradle/libs.versions.toml`
- `composeApp/src/ohosMain/kotlin/com/example/cmp_hello/sample/navigation/OhosNavigationSavedStateHandleDemo.kt`
- `composeApp/src/ohosArm64Main/kotlin/com/example/cmp_hello/sample/mainpage/OhosDisplaySections.ohosArm64.kt`
