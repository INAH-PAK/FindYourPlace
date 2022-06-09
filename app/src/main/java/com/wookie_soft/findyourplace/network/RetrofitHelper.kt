package com.wookie_soft.findyourplace.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitHelper {

    companion object{ //class 와 동반된객체 !
        fun getRetorofitInstanse(baseUrl:String):Retrofit{
            val retrofit = Retrofit.Builder()
                                   .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }

    }
}