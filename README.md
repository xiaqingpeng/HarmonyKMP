# Kotlin Multiplatform（KMP）和 Compose Multiplatform 构建，支持 Android、iOS、HarmonyOS 三端

## 核心框架

| 层次 | 技术 | 版本 |
|---|---|---|
| 多平台语言 | Kotlin Multiplatform (KMP) | `2.2.21-0.1.0` (OHOS fork) |
| 共享 UI | Compose Multiplatform | `1.9.2-OH.0.1.2-17` (OHOS fork) |
| UI 规范 | Material Design 3 | `1.9.2-OH.0.1.2-09` |
| 构建系统 | Gradle + AGP | `8.9` / `8.6.0` |

---

## 目标平台

| 平台 | 入口 | 渲染方式 |
|---|---|---|
| Android | `MainActivity` | Compose Android |
| iOS | `MainViewController` (SwiftUI bridge) | Compose UIKit |
| HarmonyOS arm64 | `MainArkUIViewController` (ArkTS NAPI bridge) | Skia 自渲染 / 统一渲染可切换 |

---

## 网络层

**Ktor** `3.3.3-0.0.1-rc1` — 多平台 HTTP 客户端

| 目标 | 引擎 |
|---|---|
| Android | `ktor-client-okhttp` |
| iOS | `ktor-client-darwin` |
| OHOS | `ktor-client-curl-ohosarm64` |

插件：Content Negotiation · kotlinx.serialization JSON · Logging

---

## 图片加载

**Coil 3** `3.3.0-0.0.1-rc1`

- `coil-core` — 核心引擎
- `coil-compose` — Compose 集成
- `coil-network-ktor3` — 网络后端（基于 Ktor）

---

## 导航

**Jetpack Navigation Compose** `2.9.4-0.1.1`

KMP 版本，支持 Android / iOS / OHOS 三端共享导航图。

---

## 数据 / 序列化

| 库 | 版本 | 用途 |
|---|---|---|
| `kotlinx.serialization` | `1.9.1-OH-004` | JSON 序列化 |
| `kotlinx.datetime` | `0.7.1-OH-001` | 跨平台日期时间 |
| `kotlinx.io` | `0.9.0-OH-001` | 跨平台 IO |
| `kotlinx.coroutines` | `1.10.2-OH-103` | 协程 |
| `atomicfu` | `0.31.0-OH-001` | 原子操作 |

---

## 加密 / 编码

| 库 | 版本 | 用途 |
|---|---|---|
| `kotlincrypto/hash` SHA-1 + MD | `0.8.0-0.0.1-rc1` | 哈希计算 |
| `matthewnelson/encoding` Base64 | `2.5.0-0.0.1-rc1` | Base64 编解码 |

---

## OHOS 特有组件

| 组件 | 说明 |
|---|---|
| `compose.multiplatform.export` | 将 Compose 运行时导出为 `.so` 供 ArkTS 调用 |
| Skiko `0.9.22.2-OH.0.1.2-07` | OHOS 版 Skia 渲染引擎 |
| C interop (`resource.def`) | 访问鸿蒙 rawfile 资源管理器（`raw_file_manager.h`） |
| NAPI bridge (`napi_init.cpp`) | 注册 `MainArkUIViewController` 等方法供 ArkTS 调用 |

---

## 自定义解决方案

**`AppIcons`** (`core/ui/AppIcons.kt`)

用纯 `ImageVector` path 实现的图标集，替代 `material-icons-extended`。原因：`material-icons-extended` 官方版本无 `ohos_arm64` 变体，无法在 OHOS native 目标上解析。

---

## 依赖分发

所有核心依赖均使用 **OHOS 定制 fork 版本**（版本号带 `-OH-` 或 `-0.0.1-rc1` 后缀），由私有 Nexus 仓库统一分发：

```
https://maven.eazytec-cloud.com/nexus/repository/maven-public/
```

涵盖 Kotlin、Compose Multiplatform、Ktor、Coil、kotlinx 全系列。与上游 JetBrains / Google 版本不直接兼容，是当前 KMP 支持鸿蒙的必要代价。

