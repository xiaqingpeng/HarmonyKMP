package com.example.cmp_hello.sample

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlin.experimental.ExperimentalNativeApi

internal object SandboxImageManager {
    var currentSandboxPath: String? by mutableStateOf(null)
        private set

    fun updateSandboxPath(newPath: String?) {
        currentSandboxPath = newPath
        println("Sandbox path updated: $newPath")
    }
}

@OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
@CName("sendBoxImagePath")
fun sendBoxImagePath(path: CPointer<ByteVar>?) {
    val filePath = path?.toKString() ?: "<null>"
    SandboxImageManager.updateSandboxPath(filePath)
}

private fun buildImageCandidates(raw: String?): List<String> {
    val input = raw?.trim().orEmpty()
    if (input.isEmpty() || input == "<null>") return emptyList()

    val tokens = if (input.contains('\n')) {
        input.lineSequence().map { it.trim() }.filter { it.isNotEmpty() }.toList()
    } else {
        listOf(input)
    }

    val result = LinkedHashSet<String>()
    tokens.forEach { token ->
        result.add(token)
        if (token.startsWith("file://")) {
            val noSchema = token.removePrefix("file://")
            if (noSchema.isNotEmpty()) {
                result.add(noSchema)
                if (!noSchema.startsWith("/")) {
                    result.add("/$noSchema")
                }
            }
        }
    }
    return result.toList()
}

@Composable
internal fun SandboxImage() {
    val sandboxPath = SandboxImageManager.currentSandboxPath
    val scrollState = rememberScrollState()
    val candidates = remember(sandboxPath) { buildImageCandidates(sandboxPath) }
    var candidateIndex by remember(sandboxPath) { mutableIntStateOf(0) }
    val imageModel = candidates.getOrNull(candidateIndex)

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("OHOS 单图回传")
        Text(sandboxPath ?: "当前还没有从 ArkTS 回传图片路径")
        Text("当前加载模型: ${imageModel ?: "<none>"}")

        ArkUIView(
            name = "selectSingleImage",
            modifier = Modifier.fillMaxWidth(),
        )

        AsyncImage(
            model = imageModel,
            contentDescription = "Sandbox Image",
            modifier = Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(width = 1.dp, color = Color.Gray),
            contentScale = ContentScale.Crop,
            onError = {
                println("SandboxImage load failed: model=$imageModel error=${it.result.throwable.message}")
                if (candidateIndex < candidates.lastIndex) {
                    candidateIndex++
                }
            },
        )
    }
}
