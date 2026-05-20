package com.example.mycompose.app

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mycompose.core.ui.AppIcons
import com.example.mycompose.core.navigation.AppRoutes

// ─────────────────────────────────────────────
// 数据模型
// ─────────────────────────────────────────────

internal data class DemoEntry(val route: String, val title: String, val category: String)

internal data class BottomNavItem(val label: String, val icon: ImageVector)

// ─────────────────────────────────────────────
// 底部导航项
// ─────────────────────────────────────────────

internal val bottomNavItems = listOf(
    BottomNavItem("首页",  AppIcons.Home),
    BottomNavItem("沸点",  AppIcons.Whatshot),
    BottomNavItem("发现",  AppIcons.Explore),
    BottomNavItem("课程",  AppIcons.PlayCircle),
    BottomNavItem("我的",  AppIcons.Person),
)

// ─────────────────────────────────────────────
// 顶部 Tab 分类（有序）
// ─────────────────────────────────────────────

private val allTabCategories = listOf(
    "基础", "布局", "状态", "输入与主题", "动画", "导航", "副作用", "KMP"
)

private val registeredDemoRoutes = setOf(
    AppRoutes.HELLO,
    AppRoutes.PROFILE_CARD,
    AppRoutes.ANIMATED_LIST,
    AppRoutes.COUNTER,
    AppRoutes.TODO,
    AppRoutes.COUNTER_UDF,
    AppRoutes.TWO_WAY_INPUT,
    AppRoutes.VALUE_ANIM,
    AppRoutes.VISIBILITY_ANIM,
    AppRoutes.LOADING_ANIM,
    AppRoutes.SWIPE_DELETE,
    AppRoutes.NAVIGATION_DEMO,
    AppRoutes.KMP_OVERVIEW,
)

// ─────────────────────────────────────────────
// 示例列表（路由 → 标题 → 分类）
// ─────────────────────────────────────────────

private val allDemoList = listOf(
    DemoEntry(AppRoutes.HELLO,             "Hello World",                "基础"),
    DemoEntry(AppRoutes.PROFILE_CARD,      "个人信息卡片",               "基础"),
    DemoEntry(AppRoutes.ANIMATED_LIST,     "LazyColumn + 点击动画",      "基础"),
    DemoEntry(AppRoutes.COUNTER,           "重组计数器",                 "基础"),
    DemoEntry(AppRoutes.SLOTS_API,         "Slots API 标题栏",           "布局"),
    DemoEntry(AppRoutes.CUSTOM_LAYOUT,     "自定义 Layout",              "布局"),
    DemoEntry(AppRoutes.STAGGERED_GRID,    "瀑布流布局",                 "布局"),
    DemoEntry(AppRoutes.CONSTRAINT,        "ConstraintLayout 表单",      "布局"),
    DemoEntry(AppRoutes.INTRINSICS,        "Intrinsics 自适应",          "布局"),
    DemoEntry(AppRoutes.TODO,              "Todo 列表（状态管理）",       "状态"),
    DemoEntry(AppRoutes.COUNTER_UDF,       "单向数据流计数器",            "状态"),
    DemoEntry(AppRoutes.TWO_WAY_INPUT,     "双向绑定输入框",              "状态"),
    DemoEntry(AppRoutes.LOGIN_FORM,        "登录表单（键盘+状态恢复）",   "输入与主题"),
    DemoEntry(AppRoutes.COMPOSITION_LOCAL, "CompositionLocal 隐式传参",  "输入与主题"),
    DemoEntry(AppRoutes.THEME_SHOWCASE,    "自定义 Material 主题",       "输入与主题"),
    DemoEntry(AppRoutes.VALUE_ANIM,        "简单值动画",                 "动画"),
    DemoEntry(AppRoutes.VISIBILITY_ANIM,   "可见性动画",                 "动画"),
    DemoEntry(AppRoutes.LOADING_ANIM,      "脉冲加载动画",               "动画"),
    DemoEntry(AppRoutes.SWIPE_DELETE,      "滑动删除列表",               "动画"),
    DemoEntry(AppRoutes.NAVIGATION_DEMO,   "多页面导航示例",             "导航"),
    DemoEntry(AppRoutes.LAUNCHED_EFFECT,   "LaunchedEffect 网络请求",    "副作用"),
    DemoEntry(AppRoutes.COROUTINE_SCOPE,   "rememberCoroutineScope",     "副作用"),
    DemoEntry(AppRoutes.DISPOSABLE,        "DisposableEffect 资源管理",  "副作用"),
    DemoEntry(AppRoutes.DERIVED_STATE,     "derivedStateOf 回到顶部",    "副作用"),
    DemoEntry(AppRoutes.SNAPSHOT_FLOW,     "snapshotFlow 状态监听",      "副作用"),
    DemoEntry(AppRoutes.KMP_OVERVIEW,      "KMP 跨平台概览",             "KMP"),
)

internal val demoList = allDemoList.filter { it.route in registeredDemoRoutes }

internal val tabCategories = allTabCategories.filter { category ->
    demoList.any { it.category == category }
}
