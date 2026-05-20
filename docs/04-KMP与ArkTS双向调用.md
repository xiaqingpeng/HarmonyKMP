# KMP 与 ArkTS 双向调用

## 1. KMP 如何调用 ArkTS

### 1.1 ArkTS 定义 controller 并发送给 Kotlin

对应文件：`harmonyApp/entry/src/main/ets/pages/ComposeInterops.ets`

```ts
import { BusinessError } from '@kit.BasicServicesKit';
import { sendKotlinCallArkTsController } from 'libentry.so';

class KotlinCallArkTsController {
  private toggleCount: number = 0;

  toggle(): void {
    this.toggleCount += 1;
    console.info(`[KotlinCallArkTsController] toggle count=${this.toggleCount}`);
  }
}

let kotlinCallArkTsController: KotlinCallArkTsController | undefined = undefined;

export function initKotlinCallArkTsController() {
  if (kotlinCallArkTsController) {
    return;
  }
  kotlinCallArkTsController = new KotlinCallArkTsController();

  // 启动阶段延迟一拍，规避 native 初始化时序抖动
  setTimeout(() => {
    if (!kotlinCallArkTsController) {
      return;
    }
    try {
      //发送 controller 给 Kotlin
      sendKotlinCallArkTsController(kotlinCallArkTsController);
      console.info('[KotlinCallArkTsController] controller sent to Kotlin');
    } catch (err) {
      const error: BusinessError = err as BusinessError;
      console.error(`[KotlinCallArkTsController] send failed: ${error.code}, ${error.message}`);
    }
  }, 0);
}
```

### 1.2 ArkTS 首页启动时执行注册

对应文件：`harmonyApp/entry/src/main/ets/pages/Index.ets`

```ts
import { initKotlinCallArkTsController } from './ComposeInterops';

aboutToAppear() {
  this.controller = nativeApi.MainArkUIViewController();
  initKotlinCallArkTsController();
}
```

### 1.3 C++ 接收 ArkTS controller 并转发给 Kotlin 导出函数

对应文件：`harmonyApp/entry/src/main/cpp/napi_init.cpp`

```cpp
extern "C" void register_kotlin_call_arkts_controller(void* controller);

static napi_value sendKotlinCallArkTsController(napi_env env, napi_callback_info info) {
    size_t argc = 1;
    napi_value args[1] = {nullptr};
    napi_get_cb_info(env, info, &argc, args, nullptr, nullptr);

    if (argc < 1 || args[0] == nullptr) {
        napi_throw_type_error(env, nullptr, "Expected controller argument");
        return nullptr;
    }

    register_kotlin_call_arkts_controller(reinterpret_cast<void*>(args[0]));
    napi_value result;
    napi_get_undefined(env, &result);
    return result;
}
```

### 1.4 C++ 导出给 ArkTS 的函数声明

对应文件：`harmonyApp/entry/src/main/cpp/napi_init.cpp`

```cpp
napi_property_descriptor desc[] = {
    {"sendKotlinCallArkTsController", nullptr, sendKotlinCallArkTsController, nullptr, nullptr, nullptr, napi_default, nullptr},
};
```

对应文件：`harmonyApp/entry/src/main/cpp/types/libentry/Index.d.ts`

```ts
export const sendKotlinCallArkTsController: (controller: object) => void;
```

### 1.5 Kotlin 接收 controller 并调用 ArkTS 方法

对应文件：`composeApp/src/ohosArm64Main/kotlin/com/example/cmp_hello/sample/KotlinCallArkTsMethodDemo.kt`

```kotlin
private object ArkTsControllerHolder {
    var controller: JsObject? = null
}

@CName("register_kotlin_call_arkts_controller")
fun registerKotlinCallArkTsController(controller: COpaquePointer?) {
    ArkTsControllerHolder.controller = try {
        controller.asNapiValue()?.let { JsObject(it) }
    } catch (t: Throwable) {
        println("registerKotlinCallArkTsController failed: ${t.message ?: t::class.simpleName}")
        null
    }
}

private fun callArkTsToggle(): String {
    val controller = ArkTsControllerHolder.controller ?: return "ArkTS 控制器未注册，请先重新进入首页。"
    return try {
        controller.call("toggle")
        "已触发 ArkTS.toggle()"
    } catch (t: Throwable) {
        "调用失败: ${t.message ?: t::class.simpleName}"
    }
}
```

### 1.6 Compose 页面点击触发调用

对应文件：`composeApp/src/ohosArm64Main/kotlin/com/example/cmp_hello/sample/KotlinCallArkTsMethodDemo.kt`

```kotlin
@Composable
internal fun KotlinCallArkTsMethodDemo() {
    var result by remember { mutableStateOf("点击下方文本后，由 Kotlin 触发 ArkTS.toggle()") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Kotlin 调 ArkTS 方法（不依赖 ArkUIView）")
        Text(
            text = "调用 ArkTS.toggle()",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { result = callArkTsToggle() }
                .padding(12.dp),
        )
        Text(result)
    }
}
```

这里真正的调用点是：

- 用户点击 `Text("调用 ArkTS.toggle()")`
- `clickable { result = callArkTsToggle() }` 触发 Kotlin 调用 ArkTS controller 的 `toggle()`
- 返回结果再回写到 Compose 状态 `result`

### 1.7 Compose 页面入口

对应文件：`composeApp/src/ohosArm64Main/kotlin/com/example/cmp_hello/sample/mainpage/OhosDisplaySections.ohosArm64.kt`

```kotlin
DisplayItem(
    title = "KotlinCallArkTsMethodDemo",
    icon = Res.drawable.compose_multiplatform,
    description = "Kotlin 直接调用 ArkTS 控制器方法。",
) { KotlinCallArkTsMethodDemo() }
```

### 1.8 调用链

```text
ArkTS aboutToAppear
  -> initKotlinCallArkTsController()
  -> setTimeout(...)-> sendKotlinCallArkTsController(controller)
  -> C++ sendKotlinCallArkTsController(...)
  -> Kotlin register_kotlin_call_arkts_controller(...)
  -> Kotlin 点击触发 callArkTsToggle()
  -> controller.call("toggle")
  -> ArkTS toggle()
```

## 2. ArkTS 调 Kotlin

### 2.1 ArkTS 类型声明

对应文件：`harmonyApp/entry/src/main/cpp/types/libentry/Index.d.ts`

```ts
export const arkTsCallKotlin: (input: number) => string;
```

### 2.2 ArkTS 调用导出方法

对应文件：`harmonyApp/entry/src/main/ets/pages/ComposeInterops.ets`

```ts
import { arkTsCallKotlin } from 'libentry.so';

const ret: string = arkTsCallKotlin(123);
this.resultText = ret;
```

### 2.3 C++ 桥接到 Kotlin 导出函数

对应文件：`harmonyApp/entry/src/main/cpp/napi_init.cpp`

```cpp
static napi_value ArkTsCallKotlin(napi_env env, napi_callback_info info) {
    int32_t input = 123;
    void* p = arkts_call_kotlin_cstr(input);

    napi_value jsStr = nullptr;
    if (p != nullptr) {
        const char* cstr = reinterpret_cast<const char*>(p);
        napi_create_string_utf8(env, cstr, NAPI_AUTO_LENGTH, &jsStr);
        arkts_free_cstr(p);
    } else {
        napi_get_undefined(env, &jsStr);
    }
    return jsStr;
}
```

### 2.4 Kotlin 导出函数

对应文件：`composeApp/src/ohosArm64Main/kotlin/com/example/cmp_hello/sample/utils/NativeBridge.kt`

```kotlin
@CName("arkts_call_kotlin_cstr")
fun arkts_call_kotlin_cstr(input: Int): CPointer<ByteVar>? { ... }

@CName("arkts_free_cstr")
fun arkts_free_cstr(ptr: CPointer<ByteVar>?) { ... }
```

### 2.5 调用链

```text
ArkTS arkTsCallKotlin(123)
  -> C++ ArkTsCallKotlin(...)
  -> Kotlin arkts_call_kotlin_cstr(...)
  -> C++ napi_create_string_utf8(...)
  -> ArkTS 获取字符串
```

