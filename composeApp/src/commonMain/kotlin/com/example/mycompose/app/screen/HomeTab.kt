package com.example.mycompose.app.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mycompose.app.components.DemoCardList
import com.example.mycompose.app.demoList
import com.example.mycompose.app.tabCategories
import kotlinx.coroutines.launch

/**
 * 首页 Tab
 * 顶部可滑动 TabRow + HorizontalPager，按分类展示所有 Demo 入口
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeTab(onNavigate: (String) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { tabCategories.size })
    val scope = rememberCoroutineScope()
    val grouped = remember { demoList.groupBy { it.category } }

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 0.dp
        ) {
            tabCategories.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(title) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val category = tabCategories[page]
            val entries = grouped[category] ?: emptyList()
            DemoCardList(entries = entries, onNavigate = onNavigate)
        }
    }
}
