package com.example.mycompose.app.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycompose.core.ui.AppIcons

// ─────────────────────────────────────────────
// 数据定义
// ─────────────────────────────────────────────

private data class DrawerItem(val icon: ImageVector, val label: String)

private val drawerGroups = listOf(
    listOf(
        DrawerItem(AppIcons.PersonAdd,        "发现好友"),
        DrawerItem(AppIcons.Create,           "创作者中心"),
    ),
    listOf(
        DrawerItem(AppIcons.BookmarkBorder,   "我的草稿"),
        DrawerItem(AppIcons.ChatBubbleOutline, "我的评论"),
        DrawerItem(AppIcons.History,          "浏览记录"),
    ),
    listOf(
        DrawerItem(AppIcons.Receipt,          "订单"),
        DrawerItem(AppIcons.ShoppingCart,     "购物车"),
        DrawerItem(AppIcons.Wallet,           "钱包"),
    ),
    listOf(
        DrawerItem(AppIcons.Groups,           "社区公约"),
    ),
)

private val drawerBottomItems = listOf(
    DrawerItem(AppIcons.PersonAdd,        "扫一扫"),
    DrawerItem(AppIcons.ChatBubbleOutline, "帮助与客服"),
    DrawerItem(AppIcons.Settings,         "设置"),
)

// ─────────────────────────────────────────────
// 抽屉面板
// ─────────────────────────────────────────────

@Composable
internal fun DrawerPanel(onClose: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 16.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 菜单分组（可滚动）
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 48.dp)
            ) {
                drawerGroups.forEach { group ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        group.forEachIndexed { index, item ->
                            DrawerMenuItem(item = item, onClick = onClose)
                            if (index < group.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }

            // 底部工具栏
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                drawerBottomItems.forEach { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable { onClose() }
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────
// 单条菜单项
// ─────────────────────────────────────────────

@Composable
private fun DrawerMenuItem(item: DrawerItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = item.label,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
