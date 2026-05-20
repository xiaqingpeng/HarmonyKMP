package com.example.cmp_hello.sample.mainpage

import cmp_hello.composeapp.generated.resources.Res
import cmp_hello.composeapp.generated.resources.compose_multiplatform
import com.example.cmp_hello.sample.ArkTsCallKotlinDemo
import com.example.cmp_hello.sample.ArkUiImageListTest
import com.example.cmp_hello.sample.CoilImageListTest
import com.example.cmp_hello.sample.CrashStackDemo
import com.example.cmp_hello.sample.GetAllPhotosPage
import com.example.cmp_hello.sample.InteropListSimple
import com.example.cmp_hello.sample.KotlinCallArkTsMethodDemo
import com.example.cmp_hello.sample.navigation.OhosNavigationSavedStateHandleDemo
import com.example.cmp_hello.sample.SandboxImage
import com.example.cmp_hello.sample.data.DisplayItem
import com.example.cmp_hello.sample.data.DisplaySection
import com.example.cmp_hello.sample.runCases

internal fun ohosArm64DisplaySections(): List<DisplaySection> = listOf(
    DisplaySection(
        title = "Navigation",
        items = listOf(
            DisplayItem(
                title = "SavedStateHandle 回传验证",
                icon = Res.drawable.compose_multiplatform,
                description = "子页写入 previousBackStackEntry.savedStateHandle，返回后检查父页是否刷新。",
            ) { OhosNavigationSavedStateHandleDemo() },
        ),
    ),
    DisplaySection(
        title = "OHOS",
        items = listOf(
            DisplayItem(
                title = "SandboxImage",
                icon = Res.drawable.compose_multiplatform,
                description = "单图选择并回传沙箱路径。",
            ) { SandboxImage() },
            DisplayItem(
                title = "选择相册图片",
                icon = Res.drawable.compose_multiplatform,
                description = "ArkTS 读取相册并将路径同步回 KMP。",
            ) { GetAllPhotosPage() },
            DisplayItem(
                title = " ArkUi 图片长列表滑动",
                icon = Res.drawable.compose_multiplatform,
                description = "ArkUI 容器批量渲染相册图片。",
            ) { ArkUiImageListTest() },
            DisplayItem(
                title = "Coil 图片长列表滑动",
                icon = Res.drawable.compose_multiplatform,
                description = "Coil 批量渲染相册图片。",
            ) { CoilImageListTest() },
            DisplayItem(
                title = "网络图片长列表滑动",
                icon = Res.drawable.compose_multiplatform,
                description = "OHOS 远程图片列表示例。",
            ) { InteropListSimple() },
            DisplayItem(
                title = "ArkTsCallKotlinDemo",
                icon = Res.drawable.compose_multiplatform,
                description = "ArkTS 调 Kotlin 并返回字符串。",
            ) { ArkTsCallKotlinDemo() },
            DisplayItem(
                title = "KotlinCallArkTsMethodDemo",
                icon = Res.drawable.compose_multiplatform,
                description = "Kotlin 直接调用 ArkTS 控制器方法。",
            ) { KotlinCallArkTsMethodDemo() },
            DisplayItem(
                title = "KotlinCallArkTsMethodDemo",
                icon = Res.drawable.compose_multiplatform,
                description = "Kotlin 直接调用 ArkTS 控制器方法。",
            ) { KotlinCallArkTsMethodDemo() },
            DisplayItem(
                title = "debug复杂对象",
                icon = Res.drawable.compose_multiplatform,
                description = "debug复杂对象。",
            ) { runCases("1") },
            DisplayItem(
                title = "CrashStackDemo",
                icon = Res.drawable.compose_multiplatform,
                description = "触发 try/finally 二次抛异常，检查崩溃栈。",
            ) { CrashStackDemo() },
        ),
    ),
)
