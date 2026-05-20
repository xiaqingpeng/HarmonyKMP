package com.example.mycompose.app.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 个人主页内容区
 * 组装 ①用户信息 ②统计/按钮 ③快捷入口 ④笔记Tab
 */
@Composable
internal fun ProfileContent(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        UserInfoSection()

        Spacer(modifier = Modifier.height(12.dp))

        StatsAndActions()

        Spacer(modifier = Modifier.height(12.dp))

        QuickEntryRow()

        Spacer(modifier = Modifier.height(8.dp))

        NoteTabSection(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}
