package com.example.cmp_hello

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
internal class OhosPlatform : Platform {
    override val name: String = "HarmonyOS"
}

@OptIn(ExperimentalNativeApi::class)
internal actual fun getPlatform(): Platform = OhosPlatform()
