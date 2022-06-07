package com.wookie_soft.findyourplace.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActicity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(binding.root)
        // setContentView를 이용하지 않고 테마를 이용해서 화면을 구성 -> 뷰를 만들지 않아 반응이 빠르고 불필요하 메모리 소모 없음. 이 화면에서는

        // 1.5초 후에 자동으로 Login Activity로 ㄱㄱ
        //android용
//        Handler(Looper.getMainLooper()).postDelayed(object :Runnable{ // 1500ms
////            override fun run() {
////                TODO("Not yet implemented")
////
////
////            }
////
////        } ,1500)
            // 이걸 람다로 죽이자 밑에 ㄱㄱ
         Handler(Looper.getMainLooper()).postDelayed({
             // 코틀린에서는 안에 파라미터가 있으면 a -> 이런식으로 쓰는거임 a=> 이거는 자바스크립트야 인아야 !!!
             startActivity(Intent(this,LoginActivity::class.java))   // this -> 익명 객체인 Runnable 참조.
             finish()
         },1500)

        // this는
//        class AAA {
//            this <- AAA 지칭
//             class BBB{
//                 this  <- BBB 지칭
//             }
//        }

    }
}