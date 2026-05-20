package com.example.cmp_hello.global

inline fun runCrashGuarded(
    tag: String,
    rethrow: Boolean = true,
    block: () -> Unit,
) {
    try {
        block()
    } catch (throwable: Throwable) {
        println(
            buildString {
                appendLine("E/CrashGuard[$tag]: caught exception before crossing runtime boundary")
                append(throwable.stackTraceToString())
            },
        )
        if (rethrow) {
            throw throwable
        }
    }
}
