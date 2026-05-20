package com.example.cmp_hello

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.serviceLoaderEnabled

@Composable
internal actual fun PlatformImageLoaderEffect() {
    val context = LocalPlatformContext.current

    LaunchedEffect(context) {
        SingletonImageLoader.setSafe {
            ImageLoader.Builder(context)
                .serviceLoaderEnabled(true)
                .build()
        }
    }
}
