package com.example.cmp_hello.sample.kotlinx

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.serviceLoaderEnabled
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.curl.Curl
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header

internal fun httpClientPlatform(
    block: HttpClientConfig<*>.() -> Unit = {},
): HttpClient =
    HttpClient(Curl) {
        engine {
            sslVerify = false
        }
        defaultRequest {
            header("X-Debug-From", "HarmonyOS")
        }
        block()
    }

@Composable
internal fun RememberOhosImageLoader() {
    val context = LocalPlatformContext.current

    LaunchedEffect(context) {
        val ktorFactory = KtorNetworkFetcherFactory(httpClient = httpClientPlatform())
        SingletonImageLoader.setSafe {
            ImageLoader
                .Builder(context)
                .components {
                    add(ktorFactory)
                }
                .serviceLoaderEnabled(true)
                .build()
        }
    }
}
