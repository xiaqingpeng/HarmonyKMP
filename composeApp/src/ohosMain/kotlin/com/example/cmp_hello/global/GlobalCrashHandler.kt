package com.example.cmp_hello.global

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ReportUnhandledExceptionHook
import kotlin.native.setUnhandledExceptionHook

private var globalCrashHandlerInstalled = false

@OptIn(ExperimentalNativeApi::class)
internal fun registerGlobalCrashHandler() {
    if (globalCrashHandlerInstalled) return
    globalCrashHandlerInstalled = true

    val unhandMe: ReportUnhandledExceptionHook = { throwable ->
        println(
            buildString {
                appendLine("GlobalCrashHandler: Kotlin unhandled exception")
                append(throwable.stackTraceToString())
            },
        )
    }
    setUnhandledExceptionHook(unhandMe)
}
