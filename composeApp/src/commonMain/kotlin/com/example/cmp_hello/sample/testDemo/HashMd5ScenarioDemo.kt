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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kotlincrypto.hash.md.MD5

private const val EMPTY_MD5_HEX = "d41d8cd98f00b204e9800998ecf8427e"
private const val ABC_MD5_HEX = "900150983cd24fb0d6963f7d28e17f72"

private fun ByteArray.toHexLower(): String =
    joinToString("") { b -> (b.toInt() and 0xFF).toString(16).padStart(2, '0') }

@Composable
internal fun HashMd5ScenarioDemo() {
    val scope = rememberCoroutineScope()

    var expected by remember {
        mutableStateOf("MD5(\"\") 与 MD5(\"abc\") 与已知向量一致")
    }
    var actual by remember { mutableStateOf("未执行") }
    var passed by remember { mutableStateOf<Boolean?>(null) }

    DemoScaffold(title = "MD5 摘要验证") {
        ScenarioTestItem(
            title = "验证 MD5",
            resultTitle = "结果",
            expectedResult = expected,
            actualResult = actual,
            passed = passed,
            buttonText = "运行验证",
            onRun = {
                scope.launch {
                    actual = "执行中..."
                    try {
                        val (emptyHex, abcHex) = withContext(Dispatchers.Default) {
                            val e = MD5().apply { update(byteArrayOf()) }.digest().toHexLower()
                            val a = MD5().apply { update("abc".encodeToByteArray()) }.digest().toHexLower()
                            e to a
                        }
                        actual = "MD5(\"\") = $emptyHex\nMD5(\"abc\") = $abcHex"
                        passed = emptyHex == EMPTY_MD5_HEX && abcHex == ABC_MD5_HEX
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
