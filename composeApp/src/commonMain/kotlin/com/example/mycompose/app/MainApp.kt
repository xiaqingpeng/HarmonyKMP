package com.example.mycompose.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.mycompose.core.navigation.AppRoutes

/**
 * 应用根 Composable
 *
 * 职责：创建 NavController，挂载根导航图。
 * 所有路由注册见 [appNavGraph]，数据定义见 [AppDemoData]。
 */
@Composable
internal fun MainApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.MAIN
    ) {
        appNavGraph(navController)
    }
}
