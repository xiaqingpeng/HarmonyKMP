package com.example.cmp_hello.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cmp_hello.sample.utils.ImagePathListManager

@Composable
internal fun GetAllPhotosPage() {
    val paths = ImagePathListManager.currentPaths
    val columnCount = ImagePathListManager.columnCount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("OHOS 多图路径回传")
        Text("当前收到 ${paths.size} 条路径")
        Text("当前网格列数：$columnCount")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            ColumnCountButton(
                text = "5 列",
                selected = columnCount == 5,
                onClick = { ImagePathListManager.updateColumnCount(5) },
            )
            Spacer(modifier = Modifier.width(12.dp))
            ColumnCountButton(
                text = "10 列",
                selected = columnCount == 10,
                onClick = { ImagePathListManager.updateColumnCount(10) },
            )
            Spacer(modifier = Modifier.width(12.dp))
            ColumnCountButton(
                text = "16 列",
                selected = columnCount == 16,
                onClick = { ImagePathListManager.updateColumnCount(16) },
            )
        }

        ArkUIView(
            name = "getAllPhotos",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(paths.take(20)) { path ->
                Text(path)
            }
        }
    }
}

@Composable
private fun ColumnCountButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    if (selected) {
        Button(onClick = onClick) {
            Text(text)
        }
        return
    }
    OutlinedButton(onClick = onClick) {
        Text(text)
    }
}
