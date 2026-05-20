package com.example.mycompose.feature.kmp.domain.model

/**
 * KMP 模块领域模型
 */
internal data class KmpTopic(
    val title: String,
    val description: String,
    val week: Int
)

internal data class TodoItemKmp(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
)

/**
 * 共享 Repository 接口（模拟 commonMain）
 * expect/actual 机制：接口在 commonMain 定义，各平台提供实现
 */
internal interface TodoRepository {
    suspend fun getTodos(): List<TodoItemKmp>
    suspend fun addTodo(title: String): TodoItemKmp
    suspend fun toggleTodo(id: Int): TodoItemKmp
    suspend fun deleteTodo(id: Int)
}
