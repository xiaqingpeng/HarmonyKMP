package com.example.mycompose.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

/**
 * 通用扩展函数
 * 放置跨功能模块复用的工具函数
 */

/** px 转 dp */
@Composable
internal fun Int.pxToDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@pxToDp.toDp() }
}

/** 安全截断字符串，超出长度时追加省略号 */
internal fun String.truncate(maxLength: Int, ellipsis: String = "..."): String {
    return if (length <= maxLength) this
    else take(maxLength - ellipsis.length) + ellipsis
}
