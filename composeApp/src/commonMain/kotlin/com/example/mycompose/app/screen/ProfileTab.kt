package com.example.mycompose.app.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mycompose.app.screen.profile.DrawerPanel
import com.example.mycompose.app.screen.profile.ProfileContent
import com.example.mycompose.app.screen.profile.ShareBottomSheet
import com.example.mycompose.core.ui.AppIcons

/**
 * 我的 Tab 入口
 *
 * 职责：Scaffold 骨架 + 左侧抽屉 + 分享 BottomSheet 开关逻辑
 * 内容拆分见 profile/ 子包：
 *   ProfileContent     → 页面内容组装
 *   ProfileHeader      → 用户信息 + 统计/按钮
 *   ProfileQuickEntry  → 快捷功能入口
 *   ProfileNoteSection → Tab栏 + 瀑布流
 *   ProfileDrawer      → 左侧抽屉面板
 *   ShareBottomSheet   → 分享底部弹窗
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileTab() {
    var drawerOpen by remember { mutableStateOf(false) }
    var shareOpen  by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0),
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { drawerOpen = true }) {
                            Icon(AppIcons.Menu, contentDescription = "菜单")
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(AppIcons.QrCodeScanner, contentDescription = "扫一扫")
                        }
                        IconButton(onClick = { shareOpen = true }) {
                            Icon(AppIcons.Share, contentDescription = "分享")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    windowInsets = WindowInsets(0)
                )
            }
        ) { padding ->
            ProfileContent(modifier = Modifier.padding(padding))
        }

        // ── 左侧抽屉遮罩 ──
        AnimatedVisibility(visible = drawerOpen, enter = fadeIn(), exit = fadeOut()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { drawerOpen = false }
            )
        }

        // ── 左侧抽屉（从左滑入）──
        AnimatedVisibility(
            visible = drawerOpen,
            modifier = Modifier.align(Alignment.CenterStart),
            enter = slideInHorizontally(initialOffsetX = { -it }),
            exit = slideOutHorizontally(targetOffsetX = { -it })
        ) {
            DrawerPanel(onClose = { drawerOpen = false })
        }
    }

    // ── 分享 BottomSheet ──
    if (shareOpen) {
        ShareBottomSheet(onDismiss = { shareOpen = false })
    }
}
