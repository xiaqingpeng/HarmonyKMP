package com.example.mycompose.core.navigation

/**
 * 全局路由常量
 * 集中管理所有导航路由，避免魔法字符串散落各处
 *
 * 命名规范：
 * - 顶层目的地：直接字符串常量
 * - 带参数路由：提供 route 模板 + 构建函数
 */
internal object AppRoutes {

    // ── 主界面 ──
    const val MAIN = "main"

    // ── 基础 ──
    const val HELLO = "hello"
    const val PROFILE_CARD = "profile_card"
    const val ANIMATED_LIST = "animated_list"
    const val COUNTER = "counter"

    // ── 布局 ──
    const val SLOTS_API = "slots_api"
    const val CUSTOM_LAYOUT = "custom_layout"
    const val STAGGERED_GRID = "staggered_grid"
    const val CONSTRAINT = "constraint"
    const val INTRINSICS = "intrinsics"

    // ── 状态 ──
    const val TODO = "todo"
    const val COUNTER_UDF = "counter_udf"
    const val TWO_WAY_INPUT = "two_way_input"

    // ── 输入与主题 ──
    const val LOGIN_FORM = "login_form"
    const val COMPOSITION_LOCAL = "composition_local"
    const val THEME_SHOWCASE = "theme_showcase"

    // ── 动画 ──
    const val VALUE_ANIM = "value_anim"
    const val VISIBILITY_ANIM = "visibility_anim"
    const val LOADING_ANIM = "loading_anim"
    const val SWIPE_DELETE = "swipe_delete"

    // ── 导航 ──
    const val NAVIGATION_DEMO = "navigation_demo"

    // ── 副作用 ──
    const val LAUNCHED_EFFECT = "launched_effect"
    const val COROUTINE_SCOPE = "coroutine_scope"
    const val DISPOSABLE = "disposable"
    const val DERIVED_STATE = "derived_state"
    const val SNAPSHOT_FLOW = "snapshot_flow"

    // ── KMP ──
    const val KMP_OVERVIEW = "kmp_overview"
}
