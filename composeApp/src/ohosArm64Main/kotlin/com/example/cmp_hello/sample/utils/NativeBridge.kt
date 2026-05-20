@file:OptIn(
    kotlin.experimental.ExperimentalNativeApi::class,
    kotlinx.cinterop.ExperimentalForeignApi::class,
)

package com.example.cmp_hello.sample.utils

import kotlinx.cinterop.*
import kotlin.experimental.ExperimentalNativeApi

@CName("arkts_call_kotlin_cstr")
fun arkts_call_kotlin_cstr(input: Int): CPointer<ByteVar>? {
    val text = "Hello from Kotlin, input=$input"
    val bytes = text.encodeToByteArray()
    val ptr: CPointer<ByteVar> = nativeHeap.allocArray(bytes.size + 1)
    for (i in bytes.indices) {
        ptr[i] = bytes[i]
    }
    ptr[bytes.size] = 0
    return ptr
}

@CName("arkts_free_cstr")
fun arkts_free_cstr(ptr: CPointer<ByteVar>?) {
    if (ptr != null) {
        nativeHeap.free(ptr.rawValue)
    }
}
