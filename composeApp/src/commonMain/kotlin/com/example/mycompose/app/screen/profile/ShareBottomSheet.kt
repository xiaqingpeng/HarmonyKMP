package com.example.mycompose.app.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycompose.core.ui.AppIcons

// ─────────────────────────────────────────────
// 数据模型
// ─────────────────────────────────────────────

private data class ShareContact(
    val name: String,
    val color: Color
)

private data class ShareChannel(
    val icon: ImageVector,
    val label: String,
    val tint: Color
)

private val shareContacts = listOf(
    ShareContact("椿芽",   Color(0xFF5C4033)),
    ShareContact("赌橘",   Color(0xFF37474F)),
    ShareContact("青柠 🍋", Color(0xFF558B2F)),
    ShareContact("momo",   Color(0xFF1565C0)),
    ShareContact("时光清浅", Color(0xFF6A1B9A)),
    ShareContact("小鱼儿",  Color(0xFF00838F)),
)

private val shareChannels = listOf(
    ShareChannel(AppIcons.Message, "私信好友", Color(0xFFE53935)),
    ShareChannel(AppIcons.People,  "微信好友", Color(0xFF43A047)),
    ShareChannel(AppIcons.People,  "朋友圈",   Color(0xFF43A047)),
    ShareChannel(AppIcons.Message, "QQ好友",   Color(0xFF1E88E5)),
)

private val shareTools = listOf(
    ShareChannel(AppIcons.Link,   "复制链接", Color.Unspecified),
    ShareChannel(AppIcons.QrCode, "我的二维码", Color.Unspecified),
)

// ─────────────────────────────────────────────
// 分享 BottomSheet
// ─────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShareBottomSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ── 标题栏 ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "分享至",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        AppIcons.Close,
                        contentDescription = "关闭",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // ── 联系人横向列表 ──
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(shareContacts) { contact ->
                    ContactItem(contact)
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ── 分享渠道（私信/微信/朋友圈/QQ）──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                shareChannels.forEach { channel ->
                    ChannelItem(channel)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── 工具行（复制链接 / 我的二维码）──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                shareTools.forEach { tool ->
                    ToolItem(tool)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ─────────────────────────────────────────────
// 子组件
// ─────────────────────────────────────────────

@Composable
private fun ContactItem(contact: ShareContact) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.clickable {}
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(contact.color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = contact.name.take(1),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = contact.name,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
private fun ChannelItem(channel: ShareChannel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.clickable {}
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(channel.tint),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = channel.icon,
                contentDescription = channel.label,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = channel.label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ToolItem(tool: ShareChannel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.clickable {}
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = tool.icon,
                contentDescription = tool.label,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = tool.label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
