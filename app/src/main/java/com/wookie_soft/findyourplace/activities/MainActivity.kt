package com.wookie_soft.findyourplace.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wookie_soft.findyourplace.R
import com.wookie_soft.findyourplace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}