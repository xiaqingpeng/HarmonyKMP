package com.example.mycompose.app.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable

@Composable
internal actual fun mainScreenContentWindowInsets(): WindowInsets =
    ScaffoldDefaults.contentWindowInsets
