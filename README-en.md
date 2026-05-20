English | [简体中文](./README-zh_CN.md)

**Version Information**

| Component | Version |
| --- | --- |
| Kotlin | 2.2.21-0.1.0 |
| Compose Multiplatform | 1.9.2-OH.0.1.2-17 |
| Gradle | 8.9 (see wrapper) |
| Android Gradle Plugin | 8.6.0 |
| Android SDK | Compile 36 / Min 24 / Target 36 |

A minimal Kotlin Multiplatform + Compose Multiplatform sample targeting **Android**, **iOS**, and **HarmonyOS**. Shared UI includes a “Click me!” button that toggles a greeting and a Compose logo image, with platform-specific labels (e.g. “HarmonyOS” on OHOS).

**Features**

- **Shared UI**: Single Compose codebase in `composeApp/src/commonMain` (Material3 theme, Button, AnimatedVisibility, Image, Text).
- **Platform entry points**: Android (`MainActivity`), iOS (`MainViewController` + SwiftUI `ContentView`), HarmonyOS (`MainArkUIViewController` + ArkTS `Compose`).
- **Resources**: Compose Multiplatform resources (`composeResources/`) are copied into the Harmony app’s rawfile with a dynamic path so `painterResource` can resolve drawables on OHOS.

**Directory Structure**

- `composeApp/src/commonMain/kotlin/com/example/cmp_hello/`
  - `App.kt` – Root Composable (button, greeting, image).
  - `Greeting.kt` – Uses `getPlatform().name` for the greeting text.
  - `Platform.kt` – `expect fun getPlatform(): Platform`.
- `composeApp/src/androidMain/.../MainActivity.kt` – Android entry.
- `composeApp/src/iosMain/.../MainViewController.kt` – iOS Compose controller.
- `composeApp/src/ohosArm64Main/.../MainArkUIViewController.kt` – OHOS entry, exports `MainArkUIViewController(env)` for ArkTS.
- `composeApp/src/commonMain/composeResources/drawable/` – Drawable used by `painterResource` (e.g. `compose-multiplatform.xml`).
- `harmonyApp/` – DevEco Studio project; receives `libkn.so`, headers, and compose resources via Gradle copy tasks.
- `iosApp/` – Xcode project; SwiftUI bridges to `MainViewController()`.

**Build & Run**

- **Android**
  ```bash
  ./gradlew :composeApp:assembleDebug
  ```
  Run from Android Studio or install the debug APK.

- **iOS**  
  Use the IDE run configuration, or open `iosApp` in Xcode and run.

- **HarmonyOS**
  1. Publish shared library and resources to the Harmony app directory:
     ```bash
     ./gradlew :composeApp:publishDebugBinariesToHarmonyApp
     ```
  2. Open the `harmonyApp` folder in DevEco Studio.
  3. Sync and run on a HarmonyOS device or emulator.

  The task copies `libkn.so`, `libkn_api.h`, and `composeApp/src/commonMain/composeResources` into `harmonyApp` so that the runtime can load Compose and drawables (path: `rawfile/composeResources/{rootProject.name}.{project.name}.generated.resources/`).

### OHOS build and package flow

From composeApp sources and resources to the harmonyApp HAP, the flow is:

```mermaid
flowchart TB
  subgraph composeApp["composeApp (Gradle / KMP)"]
    direction TB
    A1["commonMain sources<br/>App.kt, Greeting.kt, Platform.kt<br/>+ Compose deps"]
    A2["ohosArm64Main sources<br/>MainArkUIViewController.kt<br/>Platform.ohos.kt"]
    A3["composeResources<br/>drawable/compose-multiplatform.xml etc"]
    A1 --> A4["compileOhosArm64MainKotlin"]
    A2 --> A4
    A4 --> A5["linkDebugSharedOhosArm64"]
    A5 --> P1["build/bin/ohosArm64/debugShared/<br/>libkn.so"]
    A5 --> P2["build/bin/ohosArm64/debugShared/<br/>libkn_api.h"]
    A3 -.->|source dir only| C3
  end

  subgraph COPY["publishDebugBinariesToHarmonyApp (Copy)"]
    direction TB
    P1 --> C1["harmonyApp/entry/libs/arm64-v8a/<br/>libkn.so"]
    P2 --> C2["harmonyApp/entry/src/main/cpp/include/<br/>libkn_api.h"]
    A3 --> C3["harmonyApp/entry/.../rawfile/<br/>composeResources/xxx.generated.resources/"]
  end

  subgraph harmonyApp["harmonyApp (DevEco / hvigor)"]
    direction TB
    subgraph entry_native["entry native (CMake)"]
      D1["napi_init.cpp<br/>#include libkn_api.h"]
      D2["find_package(compose)<br/>compose::skikobridge"]
      D3["IMPORTED libkn.so<br/>entry/libs/arm64-v8a/"]
      D1 --> D4["add_library(entry SHARED)"]
      D2 --> D4
      D3 --> D4
      D4 --> P3["libentry.so"]
      D1 --> NAPI["Init(): androidx_compose_ui_arkui_init(env,exports)<br/>napi_define_properties: MainArkUIViewController etc<br/>napi_module_register(nm_modname=entry)"]
    end

    subgraph entry_arkts["entry ArkTS"]
      E1["oh-package: compose.har<br/>Compose ArkTS components, bridge API"]
      E2["Index.ets<br/>import nativeApi from libentry.so<br/>MainArkUIViewController(), Compose(libraryName: entry)"]
      E1 --> E2
    end

    subgraph hap["HAP package"]
      H1["ArkTS output"]
      H2["libentry.so (NAPI, links libkn.so + skikobridge)"]
      H3["libkn.so (KMP Compose runtime)"]
      H4["libskikobridge.so (from compose.har)"]
      H5["rawfile compose resources"]
      H1 --> HAP["entry-default-signed.hap"]
      H2 --> HAP
      H3 --> HAP
      H4 --> HAP
      H5 --> HAP
    end

    P3 --> H2
    C1 --> H3
    C3 --> H5
  end

  composeApp --> COPY
  COPY --> harmonyApp
```

**Notes:** **composeApp** produces **libkn.so** and **libkn_api.h** (Kotlin/Native); **composeResources** are copied as-is. The **Copy** task writes them into `harmonyApp`. **harmonyApp** builds **libentry.so** from `napi_init.cpp` (linking libkn.so and compose::skikobridge), registers NAPI with `androidx_compose_ui_arkui_init` and `MainArkUIViewController` (module name `"entry"`). ArkTS imports `libentry.so`, gets the controller, and uses **compose.har**’s `Compose({ libraryName: 'entry' })`. The HAP contains libentry.so, libkn.so, libskikobridge.so, and rawfile resources.

**Dependencies**

- Compose Runtime / Foundation / UI / Material / Material3 and Compose resources are in `composeApp/build.gradle.kts` under `commonMain`.
- OHOS: `compose.multiplatform.export`, Skiko for OHOS (e.g. `0.9.22.2-OH.0.1.2-07`), and forced export/ui/foundation versions as in the build file.

### Switching between self-rendering and unified rendering

On OHOS, Compose supports two rendering modes. In `composeApp/build.gradle.kts` under `compose { ohos { ... } }` you must **enable exactly one**:

| Mode | Config | Description |
|------|--------|-------------|
| **Self-rendering** | `skia("0.9.22.2-OH.0.1.2-07")` | Skia-based rendering (default). |
| **Unified rendering** | `ohrender("0.9.22.2-ohrende")` | Uses ArkUI unified rendering pipeline. |

**To switch**: Comment out the active line and uncomment the other. Example for unified rendering:

```kotlin
compose {
    ohos {
        // skia("0.9.22.2-OH.0.1.2-07")
        ohrender("0.9.22.2-ohrende")
    }
}
```

After changing, run `./gradlew :composeApp:publishDebugBinariesToHarmonyApp` again and rebuild the HAP in DevEco Studio.

---

## HarmonyOS common issues & solutions

### 1. Runtime crash: `ArkUIViewController_setId` or “Not mapped”

**Symptom**: App crashes on launch or when opening the Compose page; logs mention `ArkUIViewController_setId`, `Not mapped`, or `libComposeApp.so` / `libkn.so`.

**Cause**: Native `napi_init.cpp` not loading Compose init (`androidx_compose_ui_arkui_init` or `androidx_compose_ui_arkui_utils_init`), or ArkUI controller created before the native module is ready / wrong `libraryName`.

**Solution**:

1. **`napi_init.cpp`**: Use a fallback when resolving the init symbol, e.g. try `androidx_compose_ui_arkui_utils_init` then `androidx_compose_ui_arkui_init` with `dlsym(RTLD_DEFAULT, ...)` and call the non-null one.
2. **ArkTS page**: In `aboutToAppear`, call `nativeApi.MainArkUIViewController()` (with try/catch) and pass the returned controller to `Compose`.
3. **Compose component**: Set `libraryName: 'entry'` (or your dynamic library name) in the `Compose` call.

### 2. Click “Click me!” causes crash (e.g. SIGABRT / Uncaught Kotlin exception)

**Symptom**: After tapping the button, the app crashes with “Uncaught Kotlin exception” in `libkn.so`.

**Cause**: Often due to `painterResource(Res.drawable.xxx)` failing on OHOS when the Compose resources are not present under the path the runtime expects.

**Solution**: Ensure the publish task copies compose resources into Harmony’s rawfile with the same path prefix as in generated code: `composeResources/{rootProject.name}.{project.name}.generated.resources/`. The project’s `publish*BinariesToHarmonyApp` task does this dynamically; re-run it after changing `rootProject.name` or the composeApp module name.

### 3. Build error: input file does not exist (`:cinteropResourceOhosArm64`)

**Cause**: `resource.def` path in `build.gradle.kts` does not match the file location.

**Solution**: Put the def file at `composeApp/src/ohosArm64Main/cinterop/resource.def` and keep `defFile(file("src/ohosArm64Main/cinterop/resource.def"))` in the `resource` cinterop.

### 4. Dependency / variant mismatch or 403 Forbidden

**Cause**: Some AndroidX (e.g. lifecycle, savedstate) or other groups not fully compatible with OHOS, or repo permissions.

**Solution**: In `composeApp/build.gradle.kts`, use `configurations.all { resolutionStrategy { ... } }` to force needed versions and, if required, `exclude(group = "...")` for conflicting modules on OHOS. The sample already forces Compose export/ui/foundation for OHOS.

---

## Gradle build tips

- **Unresolved reference `libs.xxx`**: Define the alias in `gradle/libs.versions.toml` under `[libraries]` or `[plugins]` and use the same spelling (Gradle maps `-` to `.`, e.g. `compose-multiplatform-export` → `libs.compose.multiplatform.export`).
- **Skiko / OHOS rendering**: If you see Skiko-related crashes on OHOS, pin the Skiko version in `compose { ohos { skia("...") } }` and/or `resolutionStrategy` to an OHOS-adapted build (e.g. `0.9.22.2-OH.0.1.2-07`).

---

Learn more: [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).

---

完整中文说明见 [README-zh_CN.md](./README-zh_CN.md)。
