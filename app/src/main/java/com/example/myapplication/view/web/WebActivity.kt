package com.example.myapplication.view.web

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class WebActivity:AppCompatActivity(){
    lateinit var webView:WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        webView = findViewById(R.id.wv_conference)


        webView.webViewClient = WebViewClient()
        webView.webChromeClient = FullscreenableChromeClient(this)
        val setting = webView.settings
        setting.javaScriptEnabled = true
        setting.builtInZoomControls = true
        setting.loadWithOverviewMode=true
        setting.useWideViewPort=true
        setting.displayZoomControls = true
        setting.setSupportZoom(true)
        setting.databaseEnabled = true
        setting.domStorageEnabled = true
        setting.cacheMode = WebSettings.LOAD_NO_CACHE

        webView.loadUrl("https://aa80f639.ngrok.io/html.html")
    }
}