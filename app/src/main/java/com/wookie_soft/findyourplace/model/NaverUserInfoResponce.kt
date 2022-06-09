package com.wookie_soft.findyourplace.model

import io.grpc.internal.DnsNameResolver
//                                내가 쓸 요청 파라미터 쓰기. 순서상관없긴한데.. 걍 쓰기.
data class NaverUserInfoResponce(var resultcode:String, var message:String,var response:NidUser)
// 이 데이터 클래스는 스트링처럼 이꿜스 할 수 있음.


data class NidUser(var id:String, var email:String )// 원래 전번이나 다른것도 많은데 우린 동의항목 설정 안해놔서 어차피 안오자낭