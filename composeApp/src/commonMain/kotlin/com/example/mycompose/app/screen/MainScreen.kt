package com.example.mycompose.app.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
// import androidx.compose.ui.tooling.preview.Preview
import com.example.mycompose.app.bottomNavItems

/**
 * 主界面
 * 包含底部导航栏，根据选中 Tab 切换对应内容区
 */
@Composable
internal fun MainScreen(onNavigate: (String) -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        contentWindowInsets = mainScreenContentWindowInsets(),
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                0 -> HomeTab(onNavigate = onNavigate)
                1 -> PlaceholderTab("沸点")
                2 -> PlaceholderTab("发现")
                3 -> PlaceholderTab("课程")
                4 -> ProfileTab()
            }
        }
    }
}

// ─────────────────────────────────────────────
