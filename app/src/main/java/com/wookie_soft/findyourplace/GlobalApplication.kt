package com.wookie_soft.findyourplace

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        // https://developers.kakao.com/docs/latest/ko/getting-started/sdk-android#init 설명서
        // 내 어플리케이션 - 내 앱 - 요약정보 - 네이티브 앱 키 == appkey
        KakaoSdk.init(this, "4261a538539e260f231cbc7a745eb332") //
    }



}