package com.example.mycompose.feature.inputtheme.domain.model

/**
 * 输入与主题模块领域模型
 */
internal data class FontConfig(val bodySize: Int = 14, val titleSize: Int = 18)

internal data class UserInfo(val name: String = "游客", val isLoggedIn: Boolean = false)
