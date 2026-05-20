package com.example.cmp_hello.sample.testDemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun DemoScaffold(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
            )
            content()
        },
    )
}

@Composable
internal fun ScenarioTestItem(
    title: String,
    resultTitle: String,
    expectedResult: String,
    actualResult: String,
    passed: Boolean?,
    buttonText: String,
    onRun: () -> Unit,
) {
    val stateText = when (passed) {
        true -> "通过"
        false -> "失败"
        null -> "未执行"
    }
    val stateColor = when (passed) {
        true -> Color(0xFF15803D)
        false -> Color(0xFFB91C1C)
        null -> Color(0xFF475569)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = title, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
            Text(text = "$resultTitle 状态：$stateText", color = stateColor, fontSize = 13.sp)
            Text(text = "期望：$expectedResult", color = Color(0xFF334155), fontSize = 13.sp)
            Text(text = "实际：$actualResult", color = Color(0xFF334155), fontSize = 13.sp)
            Button(onClick = onRun) {
                Text(buttonText)
            }
        }
    }
}
