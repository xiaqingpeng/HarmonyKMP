package com.example.mycompose.app.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycompose.core.ui.AppIcons

// ─────────────────────────────────────────────
// ④ 笔记 Tab 栏 + 瀑布流内容
// ─────────────────────────────────────────────

@Composable
internal fun NoteTabSection(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("笔记", "收藏", "赞过")

    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Column {
            // 主 Tab 栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.weight(1f),
                    containerColor = Color.Transparent,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { onTabSelected(index) },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold
                                                 else FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                            },
                            selectedContentColor = MaterialTheme.colorScheme.onSurface,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = {}) {
                    Icon(
                        AppIcons.Search,
                        contentDescription = "搜索笔记",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 公开 / 私密 / 合集 子 Tab（仅"笔记"页显示）
            if (selectedTab == 0) {
                SubTabRow()
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            NoteGrid()
        }
    }
}

// ── 子 Tab ──

@Composable
private fun SubTabRow() {
    val subTabs = listOf("公开", "🔒 私密", "合集")
    var selected by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        subTabs.forEachIndexed { index, label ->
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (selected == index) FontWeight.Bold else FontWeight.Normal,
                color = if (selected == index) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable { selected = index }
            )
        }
    }
}

// ── 瀑布流笔记卡片 ──

private data class NoteCard(
    val id: Int,
    val text: String,
    val height: Int,
    val views: String
)

@Composable
private fun NoteGrid() {
    val notes = remember {
        listOf(
            NoteCard(1, "顺义南彩偶遇3公里范围内 求租 两室一厅（留言报价）看到自会回复", 220, "24"),
            NoteCard(2, "麻城中央国际城两室一厅家电齐全精装修出租", 180, "238"),
            NoteCard(3, "Jetpack Compose 动画完全指南，从入门到实战", 160, "512"),
            NoteCard(4, "Material3 主题系统深度解析", 200, "89"),
            NoteCard(5, "Navigation 最佳实践：类型安全路由", 150, "341"),
            NoteCard(6, "LazyColumn 性能优化技巧", 170, "127"),
        )
    }

    // 固定高度容器，避免与外层 verticalScroll 嵌套冲突
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            items(notes.size) { index ->
                NoteCardItem(notes[index])
            }
        }
    }
}

@Composable
private fun NoteCardItem(note: NoteCard) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            // 封面色块占位
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(note.height.dp)
                    .background(
                        when (note.id % 4) {
                            0    -> MaterialTheme.colorScheme.primaryContainer
                            1    -> MaterialTheme.colorScheme.secondaryContainer
                            2    -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
            )

            // 标题 + 点赞数
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = note.text,
                    fontSize = 12.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        AppIcons.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(11.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = note.views,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
