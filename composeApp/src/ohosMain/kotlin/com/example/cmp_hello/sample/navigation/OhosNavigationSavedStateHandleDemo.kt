package com.example.cmp_hello.sample.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

private const val HomeRoute = "saved_state_home"
private const val ChildRoute = "saved_state_child"
private const val ResultKey = "navigation_result"
private const val DefaultResult = ""
private const val DefaultDraft = "Hello from OHOS child page"
private const val LogTag = "CMP_NAV"

@Composable
internal fun OhosNavigationSavedStateHandleDemo() {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        logNav("demo start")
    }

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
    ) {
        composable(HomeRoute) {
            val currentEntry by navController.currentBackStackEntryAsState()
            val result = currentEntry
                ?.savedStateHandle
                ?.getStateFlow(ResultKey, DefaultResult)
                ?.collectAsState(initial = DefaultResult)
                ?.value ?: DefaultResult

            LaunchedEffect(result) {
                val raw = currentEntry?.savedStateHandle?.get<String>(ResultKey)
                val hasKey = currentEntry?.savedStateHandle?.contains(ResultKey)
                logNav(
                    "home observed result=${formatNavValue(result)} " +
                        "raw=${formatNavValue(raw)} hasKey=$hasKey",
                )
            }

            NavigationHomeScreen(
                result = result,
                onOpenChild = {
                    val raw = currentEntry?.savedStateHandle?.get<String>(ResultKey)
                    val hasKey = currentEntry?.savedStateHandle?.contains(ResultKey)
                    logNav(
                        "home click openChild uiResult=${formatNavValue(result)} " +
                            "raw=${formatNavValue(raw)} hasKey=$hasKey",
                    )
                    navController.navigate(ChildRoute)
                },
                onClearResult = {
                    val before = currentEntry?.savedStateHandle?.get<String>(ResultKey)
                    val hadKey = currentEntry?.savedStateHandle?.contains(ResultKey)
                    logNav(
                        "home reset before uiResult=${formatNavValue(result)} " +
                            "raw=${formatNavValue(before)} hasKey=$hadKey",
                    )
                    currentEntry?.savedStateHandle?.set(ResultKey, DefaultResult)
                    val afterSet = currentEntry?.savedStateHandle?.get<String>(ResultKey)
                    val hasKeyAfterSet = currentEntry?.savedStateHandle?.contains(ResultKey)
                    logNav(
                        "home reset afterSet uiResult=${formatNavValue(result)} " +
                            "raw=${formatNavValue(afterSet)} hasKey=$hasKeyAfterSet",
                    )
                    currentEntry?.savedStateHandle?.remove<String>(ResultKey)
                    val afterRemove = currentEntry?.savedStateHandle?.get<String>(ResultKey)
                    val hasKeyAfterRemove = currentEntry?.savedStateHandle?.contains(ResultKey)
                    logNav(
                        "home reset afterRemove uiResult=${formatNavValue(result)} " +
                            "raw=${formatNavValue(afterRemove)} hasKey=$hasKeyAfterRemove",
                    )
                },
            )
        }

        composable(ChildRoute) {
            NavigationChildScreen(
                initialDraft = DefaultDraft,
                onReturn = { draft ->
                    val before = navController.previousBackStackEntry?.savedStateHandle?.get<String>(ResultKey)
                    val hadKey = navController.previousBackStackEntry?.savedStateHandle?.contains(ResultKey)
                    logNav(
                        "child submit before raw=${formatNavValue(before)} " +
                            "hasKey=$hadKey next=$draft",
                    )
                    navController.previousBackStackEntry?.savedStateHandle?.set(ResultKey, draft)
                    val after = navController.previousBackStackEntry?.savedStateHandle?.get<String>(ResultKey)
                    val hasKey = navController.previousBackStackEntry?.savedStateHandle?.contains(ResultKey)
                    logNav(
                        "child submit after raw=${formatNavValue(after)} " +
                            "hasKey=$hasKey",
                    )
                    navController.popBackStack()
                },
            )
        }
    }
}

@Composable
private fun NavigationHomeScreen(
    result: String,
    onOpenChild: () -> Unit,
    onClearResult: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "OHOS SavedStateHandle 回传验证",
            style = MaterialTheme.typography.titleLarge,
        )
        if (result.isNotBlank()) {
            Text(
                text = "当前结果：$result",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Text(
            text = "操作步骤：打开子页，修改文本后点“写回并返回”，返回后这里会显示新值；清空时先置空再删除 key。",
            style = MaterialTheme.typography.bodyMedium,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onOpenChild) {
                Text(text = "打开子页")
            }
            OutlinedButton(onClick = onClearResult) {
                Text(text = "恢复默认")
            }
        }
    }
}

@Composable
private fun NavigationChildScreen(
    initialDraft: String,
    onReturn: (String) -> Unit,
) {
    var draft by remember { mutableStateOf(initialDraft) }

    LaunchedEffect(Unit) {
        logNav("child enter initialDraft=$initialDraft")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "子页输入",
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = "点击返回时，会把下面的内容写入 previousBackStackEntry.savedStateHandle。",
            style = MaterialTheme.typography.bodyMedium,
        )
        TextField(
            value = draft,
            onValueChange = { draft = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Button(onClick = { onReturn(draft) }) {
            Text(text = "写回并返回")
        }
    }
}

private fun logNav(message: String) {
    println("$LogTag: $message")
}

private fun formatNavValue(value: String?): String = value?.takeIf { it.isNotEmpty() } ?: "<empty>"
