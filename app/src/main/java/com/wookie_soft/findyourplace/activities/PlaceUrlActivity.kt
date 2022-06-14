package com.wookie_soft.findyourplace.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.wookie_soft.findyourplace.R
import com.wookie_soft.findyourplace.databinding.ActivityPlaceUrlBinding

class PlaceUrlActivity : AppCompatActivity() {
    val binding:ActivityPlaceUrlBinding by lazy { ActivityPlaceUrlBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // <  web view 의 기본 설정 3가지 >
        binding.wv.webViewClient = WebViewClient() // 실제 웹 뷰에는 클라이언트가 없어서, 지정해주기.
        binding.wv.webChromeClient = WebChromeClient() // 없을 수 도 잇으니 크롭도 지정
        binding.wv.settings.javaScriptEnabled = true

        val placeUrl:String = intent.getStringExtra("placeUrl") ?: ""
            binding.wv.loadUrl(placeUrl)


    }

    override fun onBackPressed() {
        if (binding.wv.canGoBack()) binding.wv.goBack()
        else super.onBackPressed()
    }
}