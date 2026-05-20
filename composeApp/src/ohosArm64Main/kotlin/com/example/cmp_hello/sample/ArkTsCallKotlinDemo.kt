package com.example.cmp_hello.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun ArkTsCallKotlinDemo() {
    Box {
        ArkUIView(
            name = "arkTsCallKotlinDemo",
            modifier = Modifier.fillMaxSize(),
        )
    }
}
