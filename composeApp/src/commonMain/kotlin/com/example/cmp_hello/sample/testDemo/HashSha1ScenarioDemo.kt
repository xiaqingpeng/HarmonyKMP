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
import org.kotlincrypto.hash.sha1.SHA1

private const val EMPTY_SHA1_HEX = "da39a3ee5e6b4b0d3255bfef95601890afd80709"
private const val ABC_SHA1_HEX = "a9993e364706816aba3e25717850c26c9cd0d89d"

private fun ByteArray.toHexLower(): String =
    joinToString("") { b -> (b.toInt() and 0xFF).toString(16).padStart(2, '0') }

private fun sha1HexOfWholeMessage(message: ByteArray): String {
    val d = SHA1()
    d.update(message)
    return d.digest().toHexLower()
}

private data class CopyBranchResult(
    val viaCopyA: String,
    val viaCopyB: String,
    val expectedA: String,
    val expectedB: String,
)

@Composable
internal fun HashSha1ScenarioDemo() {
    val scope = rememberCoroutineScope()

    var t1Expected by remember {
        mutableStateOf("对照标准测试向量（空串、已知字符串）计算 SHA-1，结果一致")
    }
    var t1Actual by remember { mutableStateOf("未执行") }
    var t1Passed by remember { mutableStateOf<Boolean?>(null) }

    var t2Expected by remember {
        mutableStateOf("copy 后两个实例分别继续计算，digest 正确且互不影响")
    }
    var t2Actual by remember { mutableStateOf("未执行") }
    var t2Passed by remember { mutableStateOf<Boolean?>(null) }

    var t3Expected by remember {
        mutableStateOf("reset 后状态恢复，后续计算结果正确")
    }
    var t3Actual by remember { mutableStateOf("未执行") }
    var t3Passed by remember { mutableStateOf<Boolean?>(null) }

    DemoScaffold(title = "org.kotlincrypto.hash:sha1 场景 Demo") {
        ScenarioTestItem(
            title = "功能测试 (1): 标准测试向量（空串、已知字符串）",
            resultTitle = "测试 (1)",
            expectedResult = t1Expected,
            actualResult = t1Actual,
            passed = t1Passed,
            buttonText = "执行测试 (1)",
            onRun = {
                scope.launch {
                    t1Actual = "执行中..."
                    try {
                        val emptyHex = withContext(Dispatchers.Default) {
                            val d = SHA1()
                            d.update(byteArrayOf())
                            d.digest().toHexLower()
                        }
                        val abcHex = withContext(Dispatchers.Default) {
                            val d = SHA1()
                            d.update("abc".encodeToByteArray())
                            d.digest().toHexLower()
                        }
                        val okEmpty = emptyHex == EMPTY_SHA1_HEX
                        val okAbc = abcHex == ABC_SHA1_HEX
                        t1Actual = "SHA1(\"\") = $emptyHex\nSHA1(\"abc\") = $abcHex"
                        t1Passed = okEmpty && okAbc
                    } catch (e: Exception) {
                        t1Actual = "异常: ${e.message}"
                        t1Passed = false
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(12.dp))

        ScenarioTestItem(
            title = "功能测试 (2): copy 后分别继续计算、互不影响",
            resultTitle = "测试 (2)",
            expectedResult = t2Expected,
            actualResult = t2Actual,
            passed = t2Passed,
            buttonText = "执行测试 (2)",
            onRun = {
                scope.launch {
                    t2Actual = "执行中..."
                    try {
                        val prefix = "hello".encodeToByteArray()
                        val suffixA = "world".encodeToByteArray()
                        val suffixB = "!!!".encodeToByteArray()
                        val msgA = "helloworld".encodeToByteArray()
                        val msgB = "hello!!!".encodeToByteArray()

                        val r = withContext(Dispatchers.Default) {
                            val expA = sha1HexOfWholeMessage(msgA)
                            val expB = sha1HexOfWholeMessage(msgB)
                            val base = SHA1()
                            base.update(prefix)
                            val c1 = base.copy()
                            val c2 = base.copy()
                            c1.update(suffixA)
                            c2.update(suffixB)
                            CopyBranchResult(
                                viaCopyA = c1.digest().toHexLower(),
                                viaCopyB = c2.digest().toHexLower(),
                                expectedA = expA,
                                expectedB = expB,
                            )
                        }
                        t2Actual =
                            "copy 路径: helloworld→${r.viaCopyA}, hello!!!→${r.viaCopyB}\n" +
                                "整段对照: helloworld→${r.expectedA}, hello!!!→${r.expectedB}"
                        t2Passed =
                            r.viaCopyA == r.expectedA &&
                                r.viaCopyB == r.expectedB &&
                                r.viaCopyA != r.viaCopyB
                    } catch (e: Exception) {
                        t2Actual = "异常: ${e.message}"
                        t2Passed = false
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(12.dp))

        ScenarioTestItem(
            title = "功能测试 (3): reset 后状态恢复、后续计算正确",
            resultTitle = "测试 (3)",
            expectedResult = t3Expected,
            actualResult = t3Actual,
            passed = t3Passed,
            buttonText = "执行测试 (3)",
            onRun = {
                scope.launch {
                    t3Actual = "执行中..."
                    try {
                        val (afterResetHex, freshHex) = withContext(Dispatchers.Default) {
                            val d = SHA1()
                            d.update("partial".encodeToByteArray())
                            d.reset()
                            d.update("abc".encodeToByteArray())
                            val r = d.digest().toHexLower()
                            val f = sha1HexOfWholeMessage("abc".encodeToByteArray())
                            r to f
                        }
                        t3Actual = "reset 后仅算 \"abc\": $afterResetHex\n整段新实例 \"abc\": $freshHex"
                        t3Passed = afterResetHex == freshHex && afterResetHex == ABC_SHA1_HEX
                    } catch (e: Exception) {
                        t3Actual = "异常: ${e.message}"
                        t3Passed = false
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}
