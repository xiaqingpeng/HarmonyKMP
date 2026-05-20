package com.example.cmp_hello.sample.mainpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cmp_hello.sample.data.DisplayItem
import com.example.cmp_hello.sample.data.DisplaySection
import org.jetbrains.compose.resources.painterResource

private val PageBackground = Color(0xFFF3F4F6)
private val HeaderBackground = Color(0xFF0F172A)
private val HeaderText = Color(0xFFF8FAFC)
private val CardBackground = Color.White
private val SectionTitleBackground = Color(0xFFE2E8F0)
private val BodyText = Color(0xFF334155)
private val TagColor = Color(0xFF2563EB)

@Composable
internal fun MainPage() {
    val sections = remember { displaySections() }
    MainPage(sections = sections)
}

@Composable
internal fun MainPage(
    sections: List<DisplaySection>,
    statusTag: String = "STEP 2 SAMPLE SHELL",
    subtitle: String = "Home list + detail page placeholders",
    homeTitle: String = "CMP Sample Home",
) {
    var currentItem by remember { mutableStateOf<DisplayItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground),
    ) {
        Header(
            currentItem = currentItem,
            onBack = { currentItem = null },
            homeTitle = homeTitle,
        )

        val selected = currentItem
        if (selected == null) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = statusTag,
                            color = TagColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = subtitle,
                            color = BodyText,
                            fontSize = 14.sp,
                        )
                    }
                }

                items(sections) { section ->
                    Section(section = section, onClick = { currentItem = it })
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(CardBackground),
            ) {
                selected.content()
            }
        }
    }
}

@Composable
private fun Header(
    currentItem: DisplayItem?,
    onBack: () -> Unit,
    homeTitle: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(HeaderBackground)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (currentItem != null) {
            Text(
                text = "Back",
                color = HeaderText,
                modifier = Modifier.clickable(onClick = onBack),
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Text(
            text = currentItem?.title ?: homeTitle,
            color = HeaderText,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun Section(
    section: DisplaySection,
    onClick: (DisplayItem) -> Unit,
) {
    Column(modifier = Modifier.padding(top = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SectionTitleBackground)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = section.title,
                color = BodyText,
                fontWeight = FontWeight.Medium,
            )
        }

        section.items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                rowItems.forEach { item ->
                    SampleCard(item = item, onClick = onClick)
                }
                repeat(2 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun RowScope.SampleCard(
    item: DisplayItem,
    onClick: (DisplayItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .height(116.dp)
            .padding(top = 12.dp, start = 8.dp, end = 8.dp)
            .background(CardBackground)
            .clickable { onClick(item) }
            .padding(horizontal = 12.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(item.icon),
            contentDescription = item.title,
            modifier = Modifier.size(36.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = item.title, fontSize = 14.sp, color = BodyText)
    }
}
