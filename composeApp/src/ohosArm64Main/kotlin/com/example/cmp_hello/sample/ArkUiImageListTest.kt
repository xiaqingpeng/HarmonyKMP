package com.example.cmp_hello.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.AdaptiveParams
import androidx.compose.ui.napi.js
import androidx.compose.ui.unit.dp
import com.example.cmp_hello.sample.utils.ImagePathListManager

private const val DenseGridColumnCount = 10
private const val DenseGridRenderRepeat = 5

@Composable
internal fun ArkUiImageListTest() {
    var pageDisposeToken by remember { mutableIntStateOf(0) }
    val columnCount = ImagePathListManager.columnCount
    val uris = ImagePathListManager.currentPaths
    val renderUris = remember(uris, columnCount) {
        if (columnCount >= DenseGridColumnCount && uris.isNotEmpty() && uris.size <= 500) {
            List(uris.size * DenseGridRenderRepeat) { index -> uris[index % uris.size] }
        } else {
            uris
        }
    }

    DisposableEffect(Unit) {
        println("CMP: PAGE ArkUiImageListTest ENTER")
        onDispose {
            pageDisposeToken++
            println("CMP: PAGE ArkUiImageListTest DISPOSE")
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(
            items = renderUris,
            key = { index, uri -> "$index-$uri" },
        ) { index, uri ->
            val tag = "grid-$index"
            val effectiveUri = if (pageDisposeToken == 0) uri else ""
            val params = remember(effectiveUri) {
                js { "Uri"(effectiveUri) }
            }
            val itemKey = "*-u1-$index-*"

            DisposableEffect(itemKey) {
                onDispose { println("CMP: DESTROY itemKey=$itemKey index=$index") }
            }

            ArkUIView(
                name = "arkImage",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.Gray),
                parameter = params,
                adaptiveParams = AdaptiveParams(maxHeight = 400.dp),
                onCreate = { println("Kotlin: ARKVIEW CREATE tag=$tag") },
                onRelease = { println("Kotlin: ARKVIEW RELEASE tag=$tag") },
                tag = tag,
            )
        }
    }
}
