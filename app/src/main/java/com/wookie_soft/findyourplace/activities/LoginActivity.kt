package com.wookie_soft.findyourplace.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.wookie_soft.findyourplace.G
import com.wookie_soft.findyourplace.databinding.ActivityLoginBinding
import com.wookie_soft.findyourplace.model.UserAccount

class LoginActivity : AppCompatActivity() {
    val binding:ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // 1. 둘러보기 버튼으로 로그인 없이 Main 화면 실행
        binding.tvGo.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

        // 2. 회원가입 버튼 클릭
        binding.tvSignUp.setOnClickListener {

            startActivity(Intent(this, SignUpActivity::class.java))

        }
        //3. 이메일 로그인
        binding.layoutEmailLogin.setOnClickListener {
            startActivity(Intent(this, EmailSignInActivity::class.java))


        }

        //4. 간편로그인 버튼들
        binding.btnKakao.setOnClickListener { clickLoginKakao() }
        binding.btnGoogle.setOnClickListener { clickLoginGoogle() }
        binding.btnNaver.setOnClickListener { clickLoginNaver() }


        // 카카로 로그인을 위한 키해시 값 얻어오기
        var keyHash:String = Utility.getKeyHash(this)
        Log.i("keyHash", keyHash) // 8zElKRQgAeFVsLPR7R5ggMtHnPI=

    }// onCreate

    private fun clickLoginKakao(){
        // 1.  카카오의 로그인에 관련된 SDK 다운해야 함.
        // 카카오는 시작하기부터 봐야 함.
        // https://developers.kakao.com/docs/latest/ko/getting-started/sdk-android

        //키해시 https://developers.kakao.com/docs/latest/ko/getting-started/sdk-android#add-key-hash-using-sdk

        // 설정 다 하고 왔냐,
        // 카카오 로그인 성공시, 반응하는 callback 객체 생성
//        val callback: (OAuthToken?, Throwable? )->Unit = fun(token:OAuthToken?, error:Throwable?){  // 자료형이 함수인 콜백 객체 , 파라미터 2개, 리턴 없음
//        }
        // 위에꺼 줄이자.
        val callback: (OAuthToken?, Throwable? )->Unit = { oAuthToken, error ->
            if(error != null){  // (에러 있다 == 로그인 실패)
                Toast.makeText(this, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "카카오 로그인 성공", Toast.LENGTH_SHORT).show()
                // 로그인 성공시 사용자 정보 요청
                UserApiClient.instance.me { user, error ->
                    if(user != null){
                        var id:String = user.id.toString()
                        var email:String = user.kakaoAccount?.email ?:"" //이메일이 null 일 수 있음. -> 엘비스 연산자로 널일때 값을 간단하게 넣을 수 있음.

                        G.userAccount = UserAccount(id, email)

                        //이제 메인엑티비티로 이동
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()

                    }
                }
            }
        }

        // 카카오톡 설치되어 있으면 -> 카카오톡 로그인
        if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
            UserApiClient.instance.loginWithKakaoTalk(this,callback= callback)
        }else{  // 없으면 -> 카카오 계정 로그인
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }



    }
    private fun clickLoginGoogle(){

    }
    private fun clickLoginNaver(){

    }




}//Main Class