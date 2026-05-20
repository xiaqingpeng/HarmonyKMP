package com.example.cmp_hello.sample.testDemo

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.matthewnelson.encoding.base64.Base64
import io.matthewnelson.encoding.core.Decoder.Companion.decodeToByteArray
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
internal fun Base64ScenarioDemo() {
    val scope = rememberCoroutineScope()

    var expected by remember {
        mutableStateOf("Base64 编码后再解码，字节与原文一致")
    }
    var actual by remember { mutableStateOf("未执行") }
    var passed by remember { mutableStateOf<Boolean?>(null) }

    DemoScaffold(title = "Base64 编解码验证") {
        ScenarioTestItem(
            title = "验证编解码",
            resultTitle = "结果",
            expectedResult = expected,
            actualResult = actual,
            passed = passed,
            buttonText = "运行验证",
            onRun = {
                scope.launch {
                    actual = "执行中..."
                    try {
                        val (ok, detail) = withContext(Dispatchers.Default) {
                            val raw = "Hello, KMP!".encodeToByteArray()
                            val b64 = raw.encodeToString(Base64.Default)
                            val back = b64.decodeToByteArray(Base64.Default)
                            val roundTrip = back.contentEquals(raw)
                            val detail =
                                "原文字节数=${raw.size}\nBase64=$b64\n往返一致=$roundTrip"
                            roundTrip to detail
                        }
                        actual = detail
                        passed = ok
                    } catch (e: Exception) {
                        actual = "异常: ${e.message}"
                        passed = false
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}
