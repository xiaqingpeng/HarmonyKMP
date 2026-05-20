package com.example.cmp_hello.sample

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.napi.JsObject
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.reinterpret
import kotlin.experimental.ExperimentalNativeApi
import platform.ohos.napi_value

@OptIn(ExperimentalForeignApi::class)
private object ArkTsControllerHolder {
    var controller: JsObject? = null
}

@OptIn(ExperimentalForeignApi::class)
private fun COpaquePointer?.asNapiValue(): napi_value? = this?.reinterpret()

@OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
@CName("register_kotlin_call_arkts_controller")
fun registerKotlinCallArkTsController(controller: COpaquePointer?) {
    ArkTsControllerHolder.controller = try {
        controller.asNapiValue()?.let { JsObject(it) }
    } catch (t: Throwable) {
        println("registerKotlinCallArkTsController failed: ${t.message ?: t::class.simpleName}")
        null
    }
    println("registerKotlinCallArkTsController: controller=$controller")
}

@OptIn(ExperimentalForeignApi::class)
private fun getArkTsController(): JsObject? {
    val controller = ArkTsControllerHolder.controller
    if (controller == null) {
        println("getArkTsController: controller is null")
    }
    return controller
}

@OptIn(ExperimentalForeignApi::class)
private fun callArkTsToggle(): String {
    val controller = getArkTsController()
    if (controller == null) {
        return "ArkTS 控制器未注册，请先重新进入首页。"
    }

    return try {
        controller.call("toggle")
        "已触发 ArkTS.toggle()"
    } catch (t: Throwable) {
        "调用失败: ${t.message ?: t::class.simpleName}"
    }
}

@Composable
internal fun KotlinCallArkTsMethodDemo() {
    var result by remember { mutableStateOf("点击下方文本后，由 Kotlin 触发 ArkTS.toggle()") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Kotlin 调 ArkTS 方法（不依赖 ArkUIView）")
        Text(
            text = "调用 ArkTS.toggle()",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { result = callArkTsToggle() }
                .padding(12.dp),
        )
        Text(result)
    }
}
