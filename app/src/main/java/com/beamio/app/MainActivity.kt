package com.beamio.app

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.beamio.app.ui.theme.BeamioTheme
import com.beamio.app.BuildConfig
class MainActivity : ComponentActivity() {
    private val cameraPermission = Manifest.permission.CAMERA
    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            // 这里你可以打个 log，或者提示一下
            Log.d("BeamioMain", "Camera permission granted? $granted")
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val webView = WebView(this).apply {
            settings.javaScriptEnabled = true

            settings.domStorageEnabled = true
            settings.databaseEnabled = true

            webViewClient = WebViewClient() // opens links inside WebView

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d(
                        "WebViewConsole",
                        "JS [${consoleMessage.messageLevel()}] @ ${consoleMessage.sourceId()}:${consoleMessage.lineNumber()} → ${consoleMessage.message()}"
                    )
                    return true
                }
            }
            loadUrl("https://beamio.app/app/") // 🔁 Replace with your URL

        }


        setContentView(webView)


    }
}

private const val TAG = "BeamioWebView"

@Composable
fun BeamioWebViewScreen() {
    val context = LocalContext.current

    var isPageLoaded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->

                WebView(ctx).apply {

                    // ⭐ 接受 Cookie（Cloudflare / 登录有时依赖）
                    val cookieManager = CookieManager.getInstance()
                    cookieManager.setAcceptCookie(true)
                    cookieManager.setAcceptThirdPartyCookies(this, true)

                    settings.apply {
                        // 必须：JS + DOM Storage
                        javaScriptEnabled = true
                        domStorageEnabled = true

                        // 有 http 资源时避免被拦截
                        mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

                        // 尽量拟合手机 Chrome 的 UA，避免站点基于 "wv" 做奇怪处理
                        userAgentString =
                            "Mozilla/5.0 (Linux; Android 14; Pixel 7) " +
                                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                    "Chrome/120.0.0.0 Mobile Safari/537.36 BeamioAppWebView"

                        // 自适应
                        loadWithOverviewMode = true
                        useWideViewPort = true


                        // 禁止缩放
                        builtInZoomControls = false
                        displayZoomControls = false
                        setSupportZoom(false)
                        textZoom = 100

                        // 一些老 API，有的框架还用得到（可选）
                        databaseEnabled = true
                        javaScriptCanOpenWindowsAutomatically = true
                    }

                    // 看得出白屏是不是背景色根本就是白的
                    setBackgroundColor(0xFFFFFFFF.toInt())


                    isHorizontalScrollBarEnabled = false
                    overScrollMode = View.OVER_SCROLL_NEVER

                    // ⭐ 打印 JS 的 console.log / error 到 Logcat
                    webChromeClient = object : WebChromeClient() {
                        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                            Log.d(
                                "BeamioWebView",
                                "JS console: ${consoleMessage?.message()} " +
                                        "(${consoleMessage?.sourceId()}:${consoleMessage?.lineNumber()})"
                            )
                            return super.onConsoleMessage(consoleMessage)
                        }
                    }

                    webViewClient = object : WebViewClient() {

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            // 让 WebView 自己处理
                            return false
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            Log.d("BeamioWebView", "onPageFinished: $url")
                            isPageLoaded = true
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            Log.e(
                                "BeamioWebView",
                                "onReceivedError: ${error?.errorCode} ${error?.description}"
                            )
                        }

                        override fun onReceivedHttpError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            errorResponse: WebResourceResponse?
                        ) {
                            super.onReceivedHttpError(view, request, errorResponse)
                            Log.e(
                                "BeamioWebView",
                                "onReceivedHttpError: ${errorResponse?.statusCode} ${errorResponse?.reasonPhrase}"
                            )
                        }
                    }

                    // ✅ 目标地址
                    loadUrl("https://beamio.app/app/")
                }
            }
        )

        // 启动图标覆盖层
        if (!isPageLoaded) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_beamio_logo),
                    contentDescription = "Beamio",
                    modifier = Modifier.size(160.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BeamioWebViewPreview() {
    BeamioTheme {
        BeamioWebViewScreen()
    }
}
