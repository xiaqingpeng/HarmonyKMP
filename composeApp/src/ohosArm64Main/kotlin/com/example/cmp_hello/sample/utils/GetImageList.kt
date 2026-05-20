package com.example.cmp_hello.sample.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlin.experimental.ExperimentalNativeApi

internal object ImagePathListManager {
    var currentPaths: List<String> by mutableStateOf(emptyList())
        private set

    var columnCount: Int by mutableIntStateOf(5)
        private set

    fun updatePaths(newPaths: List<String>) {
        currentPaths = newPaths
        println("Kotlin/Native image path count=${newPaths.size}")
    }

    fun updateColumnCount(count: Int) {
        columnCount = count
        println("Kotlin/Native column count=$count")
    }
}

@OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
@CName("sendImagePathList")
fun sendImagePathList(paths: CPointer<ByteVar>?) {
    val raw = paths?.toKString() ?: ""
    val list = if (raw.isBlank()) {
        emptyList()
    } else {
        raw.split('\n').filter { it.isNotBlank() }
    }
    ImagePathListManager.updatePaths(list)
}
