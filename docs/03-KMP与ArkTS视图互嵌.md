# KMP 与 ArkTS 视图互嵌

## 1. 两种互嵌方向

1. ArkTS 页面承载 Compose View
2. Compose 页面嵌入 ArkTS 视图

## 2. ArkTS 承载 Compose View

### 2.1 导出 Compose controller

对应文件：`composeApp/src/ohosMain/kotlin/com/example/cmp_hello/MainArkUIViewController.kt`

```kotlin
@OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
@CName("MainArkUIViewController")
fun MainArkUIViewController(env: napi_env): napi_value {
    initMainHandler(env)
    return ComposeArkUIViewController(env) {
        OhosRootApp()
    }
}
```

### 2.2 ArkTS 页面接入 controller

对应文件：`harmonyApp/entry/src/main/ets/pages/Index.ets`

```ts
import { ArkUIViewController, Compose } from 'compose';
import nativeApi from 'libentry.so';

@Entry
@Component
struct Index {
  private controller: ArkUIViewController | undefined = undefined;

  aboutToAppear() {
    this.controller = nativeApi.MainArkUIViewController();
  }

  build() {
    Column() {
      if (this.controller) {
        Compose({
          controller: this.controller,
          libraryName: 'entry',
          onBackPressed: () => this.controller!.onBackPress()
        })
      }
    }
    .width('100%')
    .height('100%')
  }
}
```

### 2.3 生命周期转发

对应文件：`harmonyApp/entry/src/main/ets/pages/Index.ets`

```ts
onPageHide() {
  if (this.controller) {
    this.controller.onPageHide();
  }
}

onBackPressed(): boolean {
  return this.controller ? this.controller.onBackPress() : false;
}
```

### 2.4 步骤

1. KMP 导出 `MainArkUIViewController`。
2. ArkTS 页面调用 `nativeApi.MainArkUIViewController()`。
3. ArkTS 使用 `Compose({ controller })` 承载 Compose 页面。
4. ArkTS 转发生命周期和返回事件。

## 3. Compose 嵌入 ArkTS 视图

### 3.1 注册 builder

对应文件：`harmonyApp/entry/src/main/ets/pages/ComposeSample.ets`

```ts
import { registerComposeInteropBuilder } from 'compose/src/main/ets/compose/ArkUIView';
import {
  getAllPhotosBuilder,
  imageBuilder,
  selectSingleImageBuilder,
  textBuilder,
} from './ComposeInterops';

export function registerComposeInteropBuilders() {
  registerComposeInteropBuilder('arkImage', imageBuilder)
  registerComposeInteropBuilder('text', textBuilder)
  registerComposeInteropBuilder('selectSingleImage', selectSingleImageBuilder)
  registerComposeInteropBuilder('getAllPhotos', getAllPhotosBuilder)
}
```

### 3.2 编写 builder

对应文件：`harmonyApp/entry/src/main/ets/pages/ComposeInterops.ets`

```ts
interface TextArgs {
  id: number;
  text: string;
  backgroundColor: string;
}

@Builder
export function textBuilder(args: TextArgs) {
  TextCard();
}

@Component
struct TextCard {
  @Consume compose_args: TextArgs;

  build() {
    Text(`${this.compose_args.id} ${this.compose_args.text}`)
      .backgroundColor(this.compose_args.backgroundColor)
      .width('100%')
      .height('100%');
  }
}
```

### 3.3 KMP 侧统一包装入口

对应文件：`composeApp/src/ohosArm64Main/kotlin/com/example/cmp_hello/sample/ArkUIView.kt`

```kotlin
@Composable
internal fun ArkUIView(
    name: String,
    modifier: Modifier,
    parameter: JsObject = js(),
    update: (JsObject) -> Unit = NoOp,
    background: Color = Color.Unspecified,
    updater: (ArkUIView) -> Unit = NoOp,
    onCreate: (ArkUIView) -> Unit = NoOp,
    onRelease: (ArkUIView) -> Unit = NoOp,
    interactive: Boolean = true,
    adaptiveParams: AdaptiveParams? = null,
    tag: String? = null,
    container: InteropContainer = InteropContainer.BACK,
) = androidx.compose.ui.interop.ArkUIView(
    name = name,
    modifier = modifier,
    parameter = parameter,
    update = update,
    background = background,
    updater = updater,
    onCreate = onCreate,
    onRelease = onRelease,
    interactive = interactive,
    adaptiveParams = adaptiveParams,
    tag = tag,
    container = container,
)
```

### 3.4 Compose 中使用 builder

不带参数：

对应文件：`composeApp/src/ohosArm64Main/kotlin/com/example/cmp_hello/sample/SandboxImage.kt`

```kotlin
ArkUIView(
    name = "selectSingleImage",
    modifier = Modifier.fillMaxWidth(),
)
```

带参数：

对应位置：
- `composeApp/src/ohosArm64Main/kotlin/com/example/cmp_hello/sample/ArkUIView.kt`
- `harmonyApp/entry/src/main/ets/pages/ComposeInterops.ets`

```kotlin
ArkUIView(
    name = "text",
    modifier = Modifier.fillMaxWidth(),
    parameter = js {
        "id"(1)
        "text"("Hello From Compose")
        "backgroundColor"("#E2E8F0")
    },
)
```

### 3.5 步骤

1. 在 ArkTS 中声明 builder。
2. 在注册文件里调用 `registerComposeInteropBuilder(...)`。
3. 在 `Index.ets` 顶部执行 `registerComposeInteropBuilders()`。
4. Compose 侧使用 `ArkUIView(name = "...")`。

## 4. 关键约束

builder 名称必须一致：

- ArkTS：`registerComposeInteropBuilder('getAllPhotos', getAllPhotosBuilder)`
- Compose：`ArkUIView(name = "getAllPhotos", ...)`

相关文件：

- `harmonyApp/entry/src/main/ets/pages/ComposeSample.ets`
- `composeApp/src/ohosArm64Main/kotlin/com/example/cmp_hello/sample/GetAllPhotosPage.kt`

