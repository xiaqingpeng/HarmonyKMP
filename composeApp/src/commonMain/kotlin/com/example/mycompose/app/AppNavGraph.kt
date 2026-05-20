package com.example.mycompose.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mycompose.app.screen.MainScreen
import com.example.mycompose.core.navigation.AppRoutes
import com.example.mycompose.core.ui.components.DemoScaffold
import com.example.mycompose.animation.PulsingLoadingIndicator
import com.example.mycompose.animation.SimpleValueAnimation
import com.example.mycompose.animation.SwipeToDeleteList
import com.example.mycompose.animation.VisibilityAnimation
import com.example.mycompose.basics.AnimatedList
import com.example.mycompose.basics.HelloWorld
import com.example.mycompose.basics.ProfileCard
import com.example.mycompose.basics.RecompositionCounter
import com.example.mycompose.kmp.KmpOverview
import com.example.mycompose.state.CounterScreen
import com.example.mycompose.state.TodoApp
import com.example.mycompose.state.TwoWayBindingInput

/**
 * 应用导航图扩展函数
 *
 * 将所有路由注册逻辑从 MainApp 中抽离，保持 MainApp 简洁。
 * 新增 feature 时只需在此文件追加对应的 composable 块。
 */
internal fun NavGraphBuilder.appNavGraph(navController: NavHostController) {

    // ── 主界面 ──
    composable(AppRoutes.MAIN) {
        MainScreen(onNavigate = { navController.navigate(it) })
    }

    // ── 基础 ──
    composable(AppRoutes.HELLO) {
        DemoScaffold("Hello World", navController) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { HelloWorld() }
        }
    }
    composable(AppRoutes.PROFILE_CARD) {
        DemoScaffold("个人信息卡片", navController) { ProfileCard() }
    }
    composable(AppRoutes.ANIMATED_LIST) {
        DemoScaffold("LazyColumn + 点击动画", navController) { AnimatedList() }
    }
    composable(AppRoutes.COUNTER) {
        DemoScaffold("重组计数器", navController) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { RecompositionCounter() }
        }
    }

    // ── 布局 ──
    /*
    composable(AppRoutes.SLOTS_API) {
        DemoScaffold("Slots API 标题栏", navController) {
            AppTopBar(
                title = { Text("Slots API 示例") },
                actions = { androidx.compose.material3.TextButton(onClick = {}) { Text("保存") } }
            )
        }
    }
    composable(AppRoutes.CUSTOM_LAYOUT) {
        DemoScaffold("自定义 Layout", navController) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                SimpleColumn(modifier = Modifier.padding(16.dp)) {
                    Text("自定义 Column 第一行")
                    Text("自定义 Column 第二行")
                    Text("自定义 Column 第三行")
                }
            }
        }
    }
    composable(AppRoutes.STAGGERED_GRID) {
        DemoScaffold("瀑布流布局", navController) { StaggeredPhotoGrid() }
    }
    composable(AppRoutes.CONSTRAINT) {
        DemoScaffold("ConstraintLayout 表单", navController) { ConstraintLayoutForm() }
    }
    composable(AppRoutes.INTRINSICS) {
        DemoScaffold("Intrinsics 自适应", navController) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { IntrinsicsDemo() }
        }
    }
    */

    // ── 状态 ──
    composable(AppRoutes.TODO) {
        TodoApp(onBack = { navController.popBackStack() })
    }
    composable(AppRoutes.COUNTER_UDF) {
        DemoScaffold("单向数据流计数器", navController) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { CounterScreen() }
        }
    }
    composable(AppRoutes.TWO_WAY_INPUT) {
        DemoScaffold("双向绑定输入框", navController) { TwoWayBindingInput() }
    }

    // ── 输入与主题 ──
    /*
    composable(AppRoutes.LOGIN_FORM) {
        DemoScaffold("登录表单", navController) { LoginForm() }
    }
    composable(AppRoutes.COMPOSITION_LOCAL) {
        DemoScaffold("CompositionLocal", navController) { CompositionLocalDemo() }
    }
    composable(AppRoutes.THEME_SHOWCASE) {
        DemoScaffold("自定义 Material 主题", navController) { ThemeShowcase() }
    }
    */

    // ── 动画 ──
    composable(AppRoutes.VALUE_ANIM) {
        DemoScaffold("简单值动画", navController) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { SimpleValueAnimation() }
        }
    }
    composable(AppRoutes.VISIBILITY_ANIM) {
        DemoScaffold("可见性动画", navController) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { VisibilityAnimation() }
        }
    }
    composable(AppRoutes.LOADING_ANIM) {
        DemoScaffold("脉冲加载动画", navController) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { PulsingLoadingIndicator() }
        }
    }
    composable(AppRoutes.SWIPE_DELETE) {
        DemoScaffold("滑动删除列表", navController) { SwipeToDeleteList() }
    }

    // ── 导航 ──
    composable(AppRoutes.NAVIGATION_DEMO) {
        DemoScaffold("多页面导航示例", navController) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(
                    text = "Navigation 示例\n（见 feature/navigation/ui/NavigationScreen.kt）",
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    // ── 副作用 ──
    /*
    composable(AppRoutes.LAUNCHED_EFFECT) {
        DemoScaffold("LaunchedEffect", navController) { LaunchedEffectDemo() }
    }
    composable(AppRoutes.COROUTINE_SCOPE) {
        DemoScaffold("rememberCoroutineScope", navController) { CoroutineScopeDemo() }
    }
    composable(AppRoutes.DISPOSABLE) {
        DemoScaffold("DisposableEffect", navController) { DisposableEffectDemo() }
    }
    composable(AppRoutes.DERIVED_STATE) {
        DemoScaffold("derivedStateOf 回到顶部", navController) { DerivedStateDemo() }
    }
    composable(AppRoutes.SNAPSHOT_FLOW) {
        DemoScaffold("snapshotFlow 状态监听", navController) { SnapshotFlowDemo() }
    }
    */

    // ── KMP ──
    composable(AppRoutes.KMP_OVERVIEW) {
        DemoScaffold("KMP 跨平台概览", navController) { KmpOverview() }
    }
}
