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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.cmp_hello.sample.utils.ImagePathListManager

private const val DenseGridColumnCount = 10
private const val DenseGridRenderRepeat = 5

@Composable
internal fun CoilImageListTest() {
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
        println("CMP: PAGE CoilImageListTest ENTER")
        onDispose { println("CMP: PAGE CoilImageListTest DISPOSE") }
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
            val itemKey = "*-u1-$index-*"
            DisposableEffect(itemKey) {
                onDispose { println("CMP: DESTROY itemKey=$itemKey index=$index") }
            }

            AsyncImage(
                model = uri,
                alignment = Alignment.Center,
                contentDescription = "Coil AsyncImage",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.Gray),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
