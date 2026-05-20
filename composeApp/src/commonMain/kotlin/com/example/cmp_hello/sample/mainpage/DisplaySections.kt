package com.example.cmp_hello.sample.mainpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cmp_hello.composeapp.generated.resources.Res
import cmp_hello.composeapp.generated.resources.compose_multiplatform
import com.example.cmp_hello.sample.data.DisplayItem
import com.example.cmp_hello.sample.data.DisplaySection
import com.example.cmp_hello.sample.testDemo.Base64ScenarioDemo
import com.example.cmp_hello.sample.testDemo.HashMd5ScenarioDemo
import com.example.cmp_hello.sample.testDemo.HashSha1ScenarioDemo

internal fun displaySections(): List<DisplaySection> = listOf(
    DisplaySection(
        title = "Crypto",
        items = listOf(
            DisplayItem(
                title = "HashSha1Scenario",
                icon = Res.drawable.compose_multiplatform,
                description = "SHA-1 标准向量、copy 分支和 reset 场景验证。",
            ) { HashSha1ScenarioDemo() },
            DisplayItem(
                title = "HashMd5Scenario",
                icon = Res.drawable.compose_multiplatform,
                description = "MD5 常用测试向量验证。",
            ) { HashMd5ScenarioDemo() },
            DisplayItem(
                title = "Base64Scenario",
                icon = Res.drawable.compose_multiplatform,
                description = "Base64 编解码往返一致性验证。",
            ) { Base64ScenarioDemo() },
        ),
    ),
    DisplaySection(
        title = "Demo",
        items = listOf(
//            displayItem(
//                title = "Interop List",
//                description = "Placeholder entry for a mixed list sample.",
//            ),
//            displayItem(
//                title = "Interop State",
//                description = "Placeholder entry for state sync samples.",
//            ),
//            displayItem(
//                title = "Nested Scroll",
//                description = "Placeholder entry for nested scroll samples.",
//            ),
        ),
    ),
    DisplaySection(
        title = "Benchmark",
        items = listOf(
//            displayItem(
//                title = "Animation",
//                description = "Placeholder entry for animation benchmark samples.",
//            ),
//            displayItem(
//                title = "Gradient",
//                description = "Placeholder entry for gradient rendering samples.",
//            ),
//            displayItem(
//                title = "Layers",
//                description = "Placeholder entry for layer composition samples.",
//            ),
//            displayItem(
//                title = "Progress",
//                description = "Placeholder entry for progress rendering samples.",
//            ),
        ),
    ),
    DisplaySection(
        title = "Material3",
        items = listOf(
//            displayItem(
//                title = "Buttons",
//                description = "Placeholder entry for Material3 button samples.",
//            ),
//            displayItem(
//                title = "Dialog",
//                description = "Placeholder entry for Material3 dialog samples.",
//            ),
//            displayItem(
//                title = "Text Field",
//                description = "Placeholder entry for Material3 text field samples.",
//            ),
        ),
    ),
)

private fun displayItem(
    title: String,
    description: String,
): DisplayItem = DisplayItem(
    title = title,
    icon = Res.drawable.compose_multiplatform,
    description = description,
) {
    PlaceholderPage(title = title, description = description)
}

@Composable
private fun PlaceholderPage(
    title: String,
    description: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = title, fontWeight = FontWeight.Bold)
        Text(text = description)
        Text(text = "Step 2 only sets up the sample shell. Real pages come later.")
    }
}
