package com.example.mycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.mycompose.app.MainApp
import com.example.mycompose.core.ui.theme.MyComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Android 专属主题（支持 Dynamic Color），内部调用共享的 MainApp
            MyComposeTheme(darkTheme = isSystemInDarkTheme()) {
                MainApp()
            }
        }
    }
}
