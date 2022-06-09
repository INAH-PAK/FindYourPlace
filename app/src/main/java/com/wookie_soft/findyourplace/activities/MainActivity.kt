package com.wookie_soft.findyourplace.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.wookie_soft.findyourplace.R
import com.wookie_soft.findyourplace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 옵션 메뉴는 꼭 자바 / 코틀린 코드로 !
        //1. 툴바를 제목줄로 설정
        setSupportActionBar(binding.toolbar)
        // 옵션메뉴 만들기 -> res -> menu 폴더 생성 -> xml 만들기 -> 액티비티가 만들어지기 전에 ㅇ액션바 생성되는 메소드 : onCreateOptionsMenu







    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_main,menu)
        return super.onCreateOptionsMenu(menu)
    }


}