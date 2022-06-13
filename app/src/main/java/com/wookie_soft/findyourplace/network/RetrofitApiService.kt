package com.wookie_soft.findyourplace.network

import com.wookie_soft.findyourplace.model.NaverUserInfoResponce
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitApiService {

    // 6/9 40:56 헤더
    // 네아로(네이버 아이디 로그인 ) 사용자 정보 API
    @GET("v1/nid/me")
    fun getNaverIdUserInfo(@Header("Authorization") authorization:String ): Call<NaverUserInfoResponce>
    // 요청 헤더 : Authorization
    // 어노테이션 == 반드시 해석해야하는 주석
    // @Header("Authorization") : 서버단에서 반드이 헤더로 Authorization이 왔다 ! 라고 해석하고
    // authorization == 파라미터로 이 값을 보낸고당.
    // Headers : 값이 고정되어 있음, Header : 값을 동적으로 받을 수 있음. <- 파라미터로 전달받을 수 있다는 얘기.


    // 6/13 카카오 키워드 장소검색 API -> 결과를 먼저 String으로 보자. 파싱은 그 다음에
    @Headers("Authorization: KakaoAK 0da232ba003e1dbcaaf99f95dc4761ab")
    @GET("/v2/local/search/keyword.json")
    fun searchPlacesToString(@Query("query") query: String, @Query("x") longitude:String, @Query("y") latitude:String):Call<String>
                           // query , longitude , latitude => 내가 보낼 요청값들  ////     @Query("query") . . => 서버에서 내가 보낸 값을 이걸로 알아들음




}