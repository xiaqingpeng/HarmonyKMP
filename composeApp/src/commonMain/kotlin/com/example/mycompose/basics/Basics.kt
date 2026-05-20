package com.example.mycompose.basics

/**
 * 第1周：Compose 入门与基础布局
 * 涵盖：Composable函数、Row/Column/Box布局、LazyColumn列表、状态、动画、重组
 */

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
// import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycompose.core.ui.theme.MyComposeTheme

// ─────────────────────────────────────────────
// 周一：Hello World + 基础 Composable 函数
// ─────────────────────────────────────────────

/**
 * 最简单的 Composable 函数示例
 * 知识点：@Composable 注解、函数即UI
 */
@Composable
internal fun HelloWorld() {
    Text(
        text = "Hello, Jetpack Compose!",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

// ─────────────────────────────────────────────
// 周二：Row / Column / Box 布局 → 个人信息卡片
// ─────────────────────────────────────────────

/**
 * 个人信息卡片
 * 知识点：Column垂直排列、Row水平排列、Box层叠、Modifier链式调用
 */
@Composable
internal fun ProfileCard(
    name: String = "张三",
    title: String = "Android 开发工程师",
    email: String = "zhangsan@example.com"
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 头像占位（Box层叠示例）
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.first().toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 文字信息（Column垂直排列）
            Column {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = email,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
// 周三：LazyColumn 列表 + 点击动画
// ─────────────────────────────────────────────

internal data class ListItem(val id: Int, val title: String, val subtitle: String)

/**
 * LazyColumn 列表示例
 * 知识点：LazyColumn、items、key优化、点击状态
 */
@Composable
internal fun AnimatedList() {
    val items = remember {
        List(20) { i -> ListItem(i, "标题 ${i + 1}", "这是第 ${i + 1} 条数据的描述") }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it.id }) { item ->
            AnimatedListItem(item)
        }
    }
}

@Composable
internal fun AnimatedListItem(item: ListItem) {
    var expanded by remember { mutableStateOf(false) }

    // animateDpAsState：点击时高度动画
    val cardHeight by animateDpAsState(
        targetValue = if (expanded) 100.dp else 60.dp,
        animationSpec = tween(durationMillis = 300),
        label = "cardHeight"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize() // 内容大小动画
        ) {
            Text(text = item.title, fontWeight = FontWeight.Medium)
            if (expanded) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
// 周五：重组（Recomposition）计数器
// ─────────────────────────────────────────────

/**
 * 计数器示例
 * 知识点：remember、mutableStateOf、重组触发条件
 * 关键：只有读取了 state 的 Composable 才会重组
 */
@Composable
internal fun RecompositionCounter() {
    var count by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        Text(
            text = "点击次数：$count",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // CountDisplay 只读取 count，所以只有它会重组
        CountDisplay(count = count)

        Button(onClick = { count++ }) {
            Text("点击 +1")
        }

        Button(
                onClick = { count = 0 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("重置")
            }
        }
    }
}

// 独立的展示组件，演示重组隔离
@Composable
private fun CountDisplay(count: Int) {
    val backgroundColor = when {
        count == 0 -> Color.LightGray
        count < 5 -> Color(0xFF90CAF9)
        count < 10 -> Color(0xFF66BB6A)
        else -> Color(0xFFEF5350)
    }

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$count",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

// ─────────────────────────────────────────────

