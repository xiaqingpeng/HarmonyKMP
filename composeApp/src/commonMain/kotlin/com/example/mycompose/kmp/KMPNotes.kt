package com.example.mycompose.kmp

/**
 * 第8周：KMP 跨平台整合
 *
 * KMP（Kotlin Multiplatform）允许在 Android、iOS、桌面端共享业务逻辑代码。
 * 本文件展示 KMP 项目中的核心模式，实际 KMP 项目需要单独创建多模块项目。
 *
 * KMP 项目结构：
 * ├── shared/                    ← 共享模块
 * │   ├── commonMain/            ← 所有平台共享的代码
 * │   │   ├── data/              ← 数据模型、Repository
 * │   │   ├── domain/            ← 业务逻辑、UseCase
 * │   │   └── presentation/      ← ViewModel（共享）
 * │   ├── androidMain/           ← Android 特定实现
 * │   └── iosMain/               ← iOS 特定实现
 * ├── androidApp/                ← Android UI（Jetpack Compose）
 * └── iosApp/                    ← iOS UI（SwiftUI）
 */

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
// import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mycompose.core.ui.theme.MyComposeTheme

// ─────────────────────────────────────────────
// 模拟 KMP 共享模块中的代码
// ─────────────────────────────────────────────

/**
 * 共享数据模型（commonMain）
 * 在真实 KMP 项目中，这些类放在 shared/commonMain
 */
internal data class TodoItemKmp(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
)

/**
 * 共享 Repository 接口（commonMain）
 * expect/actual 机制：接口在 commonMain 定义，各平台提供实现
 */
internal interface TodoRepository {
    suspend fun getTodos(): List<TodoItemKmp>
    suspend fun addTodo(title: String): TodoItemKmp
    suspend fun toggleTodo(id: Int): TodoItemKmp
    suspend fun deleteTodo(id: Int)
}

/**
 * 共享 ViewModel（commonMain）
 * 使用 kotlinx-coroutines 和 kotlinx.collections.immutable
 * 在 Android 端用 androidx.lifecycle.ViewModel 包装
 */
internal class TodoViewModelKmp(private val repository: TodoRepository) {
    // 在真实 KMP 中使用 StateFlow
    // val todos: StateFlow<List<TodoItemKmp>> = ...
}

// ─────────────────────────────────────────────
// expect/actual 机制示例
// ─────────────────────────────────────────────

/**
 * expect/actual：平台差异化实现
 *
 * commonMain 中声明 expect：
 *   expect fun getPlatformName(): String
 *
 * androidMain 中提供 actual：
 *   actual fun getPlatformName(): String = "Android ${Build.VERSION.SDK_INT}"
 *
 * iosMain 中提供 actual：
 *   actual fun getPlatformName(): String = UIDevice.currentDevice.systemName()
 */

// 模拟 expect/actual（在单模块项目中直接实现）
internal fun getPlatformName(): String = "Android"

// ─────────────────────────────────────────────
// KMP 知识点总览 UI
// ─────────────────────────────────────────────

internal data class KmpTopic(val title: String, val description: String, val week: Int)

@Composable
internal fun KmpOverview() {
    val topics = remember {
        listOf(
            KmpTopic("expect/actual", "平台差异化实现机制，commonMain 声明接口，各平台提供实现", 8),
            KmpTopic("共享 ViewModel", "使用 kotlinx-coroutines 的 StateFlow 在所有平台共享状态管理", 8),
            KmpTopic("Ktor（网络请求）", "KMP 跨平台 HTTP 客户端，替代 Retrofit", 8),
            KmpTopic("SQLDelight（本地存储）", "KMP 跨平台数据库，生成类型安全的 SQL 代码", 8),
            KmpTopic("Compose Multiplatform", "Jetbrains 的跨平台 UI 框架，Android/iOS/桌面端共享 UI", 8),
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "KMP 跨平台",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "当前平台：${getPlatformName()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("KMP 项目结构", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
shared/
  commonMain/   ← 共享业务逻辑
    data/       ← 数据模型、Repository
    domain/     ← UseCase
    presentation/ ← ViewModel
  androidMain/  ← Android 特定实现
  iosMain/      ← iOS 特定实现
androidApp/     ← Compose UI
iosApp/         ← SwiftUI
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }

        items(topics) { topic ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(topic.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(topic.description, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

