package com.example.mycompose.feature.state.domain.model

/**
 * 状态模块领域模型
 */
internal data class TodoItem(
    val id: Int,
    val text: String,
    val isCompleted: Boolean = false
)
