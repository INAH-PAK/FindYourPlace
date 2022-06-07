package com.wookie_soft.findyourplace

import com.wookie_soft.findyourplace.model.UserAccount

class G {
    // 글로벌 클래스 : 코틀린 파일로 만들면 자바로 해석하기 힘들어서 글로벌 클래스 많이 만듬. -> 회사는 보통 G보다 gloval 로 많이 쓰임
    // 이 글로벌 클래스는 교수님은 어디 소속된 느낌이 아니라 모델 패키지가 아닌 아예 플랫하게 걍 만듬, (패키지 안넣고)

    companion object{
        var userAccount:UserAccount?= null
    }
}