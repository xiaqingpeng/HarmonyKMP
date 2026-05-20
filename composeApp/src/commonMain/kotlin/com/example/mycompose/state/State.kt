package com.example.mycompose.state

/**
 * 第3周：状态管理核心
 * 涵盖：无状态/有状态组件、单向数据流、状态提升、remember、MutableState、Todo列表
 */

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.mycompose.core.ui.AppIcons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.mycompose.core.ui.theme.MyComposeTheme

// ─────────────────────────────────────────────
// 周一：无状态 vs 有状态组件
// ─────────────────────────────────────────────

/**
 * 无状态组件（Stateless）：只接收数据，不持有状态
 * 优点：可复用、易测试、可预览
 */
@Composable
internal fun StatelessListItem(
    text: String,
    isCompleted: Boolean,
    onCheckedChange: (Boolean) -> Unit  // 事件向上传递
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isCompleted,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            textDecoration = if (isCompleted) TextDecoration.LineThrough else null,
            color = if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * 有状态组件（Stateful）：内部持有状态
 * 适合：不需要外部控制状态的独立组件
 */
@Composable
internal fun StatefulListItem(text: String) {
    var isCompleted by remember { mutableStateOf(false) }

    StatelessListItem(
        text = text,
        isCompleted = isCompleted,
        onCheckedChange = { isCompleted = it }
    )
}

// ─────────────────────────────────────────────
// 周二：单向数据流（UDF）+ 状态提升
// ─────────────────────────────────────────────

/**
 * 单向数据流计数器
 * 知识点：状态向下流动（count → UI），事件向上传递（onIncrement/onDecrement）
 *
 *   ┌─────────────────────────────┐
 *   │  State: count               │  ← 状态在父组件
 *   │  ↓ (数据向下)               │
 *   │  CounterDisplay(count)      │
 *   │  ↑ (事件向上)               │
 *   │  onIncrement / onDecrement  │
 *   └─────────────────────────────┘
 */
@Composable
internal fun CounterScreen() {
    var count by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CounterContent(
            count = count,
            onIncrement = { count++ },
            onDecrement = { if (count > 0) count-- },
            onReset = { count = 0 }
        )
    }
}

// 纯展示组件，无状态
@Composable
internal fun CounterContent(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "计数器",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "$count",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onDecrement) { Text("-") }
            Button(onClick = onIncrement) { Text("+") }
        }

        TextButton(onClick = onReset) { Text("重置") }
    }
}

// ─────────────────────────────────────────────
// 周三：remember 与跨重组状态保存
// ─────────────────────────────────────────────

/**
 * remember 对比示例
 * 知识点：有 remember 的状态在重组间保持，无 remember 每次重组都重置
 */
@Composable
internal fun RememberDemo() {
    // ✅ 有 remember：重组后值保持
    var countWithRemember by remember { mutableStateOf(0) }

    // ❌ 无 remember：每次重组都重置为 0（这里用 derivedStateOf 演示）
    // var countWithoutRemember = mutableStateOf(0)  // 每次重组都是新对象

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("有 remember 的计数器（重组后保持）")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$countWithRemember",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.width(60.dp)
            )
            Button(onClick = { countWithRemember++ }) { Text("+1") }
        }

        HorizontalDivider()

        Text(
            text = "关键：remember 让值在重组间存活\n" +
                   "rememberSaveable 还能在配置变更（旋转屏幕）后恢复",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ─────────────────────────────────────────────
// 周四：MutableState 双向绑定输入框
// ─────────────────────────────────────────────

/**
 * MutableState 双向绑定
 * 知识点：TextField 的 value/onValueChange 模式
 */
@Composable
internal fun TwoWayBindingInput() {
    var text by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },  // 状态随输入更新
            label = { Text("输入内容") },
            placeholder = { Text("在这里输入...") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(onClick = { text = "" }) {
                        Icon(AppIcons.Delete, contentDescription = "清空")
                    }
                }
            }
        )

        Text(
            text = "字符数：${text.length}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Button(
            onClick = { submitted = text; text = "" },
            enabled = text.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("提交")
        }

        if (submitted.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "已提交：$submitted",
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
// 周末项目：Todo 列表（添加/删除/标记完成）
// ─────────────────────────────────────────────

internal data class TodoItem(
    val id: Int,
    val text: String,
    val isCompleted: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TodoApp(onBack: (() -> Unit)? = null) {
    var todos by remember {
        mutableStateOf(
            listOf(
                TodoItem(1, "学习 Composable 函数"),
                TodoItem(2, "掌握 Row/Column/Box 布局"),
                TodoItem(3, "理解状态与重组")
            )
        )
    }
    var inputText by remember { mutableStateOf("") }
    var nextId by remember { mutableStateOf(4) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo 列表 (${todos.count { !it.isCompleted }} 未完成)") },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = AppIcons.ArrowBack,
                                contentDescription = "返回"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // 输入区域
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("添加新任务...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            todos = todos + TodoItem(nextId++, inputText.trim())
                            inputText = ""
                        }
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(AppIcons.Add, contentDescription = "添加")
                }
            }

            HorizontalDivider()

            // 列表区域
            LazyColumn {
                items(todos, key = { it.id }) { todo ->
                    TodoListItem(
                        todo = todo,
                        onToggle = { id ->
                            todos = todos.map {
                                if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
                            }
                        },
                        onDelete = { id ->
                            todos = todos.filter { it.id != id }
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
private fun TodoListItem(
    todo: TodoItem,
    onToggle: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isCompleted,
            onCheckedChange = { onToggle(todo.id) }
        )
        Text(
            text = todo.text,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null,
            color = if (todo.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface
        )
        IconButton(onClick = { onDelete(todo.id) }) {
            Icon(
                AppIcons.Delete,
                contentDescription = "删除",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}


