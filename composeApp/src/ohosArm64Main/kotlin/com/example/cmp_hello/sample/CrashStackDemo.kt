package com.example.cmp_hello.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cmp_hello.global.runCrashGuarded

@Composable
internal fun CrashStackDemo() {
    var message by remember { mutableStateOf("manual crash demo") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("点击下面按钮触发 Kotlin 崩溃，检查最终崩溃栈是否异常。")

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Crash message") },
            singleLine = true,
        )

        Button(
            onClick = {
                runCrashGuarded(tag = "CrashStackDemo", rethrow = false) {
                    testCrash(message)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("触发崩溃")
        }
    }
}

internal fun testCrash(msg: String) {
    try {
        throw Exception("test crash 1: $msg")
    } finally {
        RmsLog.e("KmpTest", "test crash: $msg")
//        throw Exception("test crash 2")
    }
}

internal object RmsLog {
    fun e(tag: String, message: String) {
        println("E/$tag: $message")
    }
}
