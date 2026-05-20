package com.example.mycompose.animation

/**
 * 第5周：动画与手势交互
 * 涵盖：简单值动画、可见性动画、内容大小动画、多值动画、手势、滚动、拖动、滑动
 */

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.example.mycompose.core.ui.AppIcons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
// import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycompose.core.ui.theme.MyComposeTheme
import kotlin.math.roundToInt

// ─────────────────────────────────────────────
// 周一：简单值动画 + 可见性动画
// ─────────────────────────────────────────────

/**
 * animateXxxAsState：最简单的动画 API，状态变化时自动插值
 * 知识点：animateDpAsState、animateColorAsState、animateFloatAsState
 */
@Composable
internal fun SimpleValueAnimation() {
    var expanded by remember { mutableStateOf(false) }

    // 尺寸动画
    val size by animateDpAsState(
        targetValue = if (expanded) 200.dp else 80.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "size"
    )

    // 颜色动画
    val color by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.secondary,
        animationSpec = tween(durationMillis = 500),
        label = "color"
    )

    // 圆角动画
    val cornerRadius by animateDpAsState(
        targetValue = if (expanded) 16.dp else 50.dp,
        label = "corner"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(cornerRadius))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (expanded) "点击收起" else "点击展开",
                color = Color.White,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "收起" else "展开")
        }
    }
}

/**
 * AnimatedVisibility：内容出现/消失动画
 * 知识点：enter/exit 动画组合
 */
@Composable
internal fun VisibilityAnimation() {
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "隐藏" else "显示")
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "我会淡入淡出 + 滑动出现/消失",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
// ─────────────────────────────────────────────

/**
 * 自定义加载指示器
 * 知识点：rememberInfiniteTransition、infiniteRepeatable、animateFloat
 */
@Composable
internal fun PulsingLoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    // 大小脉冲动画
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // 透明度动画
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    // 颜色动画
    val color by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primary,
        targetValue = MaterialTheme.colorScheme.tertiary,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
                .graphicsLayer { this.alpha = alpha }
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text("加载中", color = Color.White, fontSize = 12.sp)
        }

        Text("正在加载数据...", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// ─────────────────────────────────────────────
// 周三：手动动画（Animatable）+ 点击手势
// ─────────────────────────────────────────────

/**
 * 按钮点击缩放效果
 * 知识点：Animatable、pointerInput、detectTapGestures
 */
/*
@Composable
internal fun ClickScaleButton(
    text: String = "点击我",
    onClick: () -> Unit = {}
) {
    val scale = remember { Animatable(1f) }

    Button(
        onClick = {},
        modifier = Modifier
            .scale(scale.value)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        // 按下：缩小
                        scale.animateTo(0.85f, animationSpec = tween(100))
                        tryAwaitRelease()
                        // 松开：弹回
                        scale.animateTo(
                            1f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                        )
                        onClick()
                    }
                )
            }
    ) {
        Text(text)
    }
}
*/

// ─────────────────────────────────────────────
// 周四：嵌套滚动（头部折叠效果）
// ─────────────────────────────────────────────

/**
 * 折叠头部效果
 * 知识点：verticalScroll + 根据滚动偏移量计算头部高度
 */
@Composable
internal fun CollapsingHeader() {
    val scrollState = rememberScrollState()

    // 根据滚动量计算头部高度（200dp → 56dp）
    val headerHeight by remember {
        derivedStateOf {
            val maxHeight = 200f
            val minHeight = 56f
            val scrollFraction = (scrollState.value / 400f).coerceIn(0f, 1f)
            (maxHeight - (maxHeight - minHeight) * scrollFraction).dp
        }
    }

    val headerAlpha by remember {
        derivedStateOf {
            1f - (scrollState.value / 300f).coerceIn(0f, 1f)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 可滚动内容
        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            // 头部占位
            Spacer(modifier = Modifier.height(200.dp))

            // 内容列表
            repeat(20) { i ->
                ListItem(
                    headlineContent = { Text("列表项 ${i + 1}") },
                    supportingContent = { Text("这是第 ${i + 1} 条内容的描述") }
                )
                HorizontalDivider()
            }
        }

        // 固定头部（覆盖在内容上方）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "折叠头部",
                color = Color.White.copy(alpha = headerAlpha),
                fontSize = 24.sp
            )
        }
    }
}

// ─────────────────────────────────────────────
// 周五：拖动手势
// ─────────────────────────────────────────────

/**
 * 可拖动的元素
 * 知识点：detectDragGestures、offset、IntOffset
 */
@Composable
internal fun DraggableBox() {
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text("拖我", color = Color.White)
        }
    }
}

// ─────────────────────────────────────────────
// 周末：滑动删除 Todo 列表
// ─────────────────────────────────────────────

/**
 * 滑动删除
 * 知识点：SwipeToDismiss（Material3 的 SwipeToDismissBox）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SwipeToDeleteList() {
    var items by remember {
        mutableStateOf(List(8) { "任务 ${it + 1}：学习 Compose 动画" })
    }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(items, key = { it }) { item ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        items = items - item
                        true
                    } else false
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    // 滑动时显示的红色背景
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.error),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            AppIcons.Delete,
                            contentDescription = "删除",
                            tint = Color.White,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                },
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
