package com.example.cmp_hello.sample

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import androidx.compose.foundation.shape.CircleShape

@Composable
internal fun InteropListSimple() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
    ) {
        items(count = 30) { index ->
            SubcomposeAsyncImage(
                modifier = Modifier.padding(vertical = 8.dp).size(320.dp),
                model = "https://picsum.photos/200/300?random=$index",
                contentDescription = "Network image $index",
                contentScale = ContentScale.Crop,
                loading = {
                    Box(Modifier.fillMaxSize()) {
                        val transition = rememberInfiniteTransition()
                        val yOffset by transition.animateFloat(
                            initialValue = -120f,
                            targetValue = 120f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(500),
                                repeatMode = RepeatMode.Reverse,
                            ),
                        )
                        Spacer(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp)
                                .offset { IntOffset(0, yOffset.toInt()) }
                                .clip(CircleShape)
                                .background(Color(0xFF2563EB)),
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color(0xFFE5E7EB)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "图片加载失败", color = Color(0xFF475569))
                    }
                },
            )
        }
    }
}
