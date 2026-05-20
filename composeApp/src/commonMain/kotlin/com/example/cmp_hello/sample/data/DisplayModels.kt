package com.example.cmp_hello.sample.data

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.DrawableResource

internal data class DisplayItem(
    val title: String,
    val icon: DrawableResource,
    val description: String,
    val content: @Composable () -> Unit,
)

internal data class DisplaySection(
    val title: String,
    val items: List<DisplayItem>,
)
