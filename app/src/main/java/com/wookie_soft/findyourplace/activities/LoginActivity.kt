package com.wookie_soft.findyourplace.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.wookie_soft.findyourplace.G
import com.wookie_soft.findyourplace.databinding.ActivityLoginBinding
import com.wookie_soft.findyourplace.model.NaverUserInfoResponce
import com.wookie_soft.findyourplace.model.UserAccount
import com.wookie_soft.findyourplace.network.RetrofitApiService
import com.wookie_soft.findyourplace.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        binding.btnKakao.setOnClickListener { clickLoginKakao() } // 카카오 -> SDK Method
        binding.btnGoogle.setOnClickListener { clickLoginGoogle() } // 구글 -> Intent
        binding.btnNaver.setOnClickListener { clickLoginNaver() } // 네이버 -> REST API

        // 반짝 문제 : REST ( Representational State Transfer ) API 란?
        // 웹 상에 존재하는 모든 자원에 URI를 부여하는 방식입니다. HTTP 웹 표준을 이용하여 http의 캐싱을 활용할 수 있다는 장점이 있고,
        // 클라이언트와 서버에서 개발해야 할 부분이 명확해지고 Uri만 어떤 요청인지 파악될 수 있는 직관적이라는 특징이 있습니다.



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

        //Firebase Authentication - 공급업체 ID ( Google)
        // https://console.firebase.google.com/project/findyourlocation-a8b17/authentication/users
        //  SHA-1 키 받는법
        // 오른쪽 상단보면 옆에 Gradle -> Tasks -> android -> signingReport  더블클릭 시 콘솔에 나옴.

        // 구글 로그인 관련 가이드 문서를 파이어베이스에서 보면 무 조 건 Firebase의 Auth제품과 연동하도록 안내함.
        // => 근데 나는 필요없단말야?
        // ==> 그래서 구글 로그인만 하려면, 차라리 구글 개발자 사이트의 가이드 문서를 참고할 것을 권장 !!!

        // 근데 firebase와 Google 계정은 연결되어 있어서
        // https://console.cloud.google.com/apis/credentials?project=findyourlocation-a8b17
        // 구글 콘솔 가보면 파이어 베이스에서 만든 프로젝트가 여기도 있음!!!!
        // API 키 자동으로 만들어져있음 ~~~
        // 내 프로젝트 사용자 인증 정보 :  https://console.cloud.google.com/apis/credentials?project=findyourlocation-a8b17
        // 요기서 웹!!!!!안드로이드 말고 웹!! 클라이언트 아이디 필요 !!
        // 구글로 로그인 하면 웹 앱으로 로그인 화면 띄워주는거라서 그럼.

        // 1. 구글 로그인 옵션 객체 생성 - 빌더 이용 : https://developers.google.com/identity/sign-in/android/sign-in
        val gso : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // 2. 구글 로그인 화면을 보여주는 액티비티를 실행시켜주는 Intent 객체 얻어오기  :  https://developers.google.com/identity/sign-in/android/sign-in#add_the_google_sign-in_button_to_your_app
        val intent:Intent = GoogleSignIn.getClient(this,gso).signInIntent // GoogleSignIn 객테에게 옵션주고 그거에 맞는 get인텐트 언어온거임.
        // 밑에 만든
        resultLauncher.launch(intent) // !!!!! 얘가 startActivityForResult !!!!  먼저 이 액티비티

    }
    // 새로운 액티비티를 실행하고 그 결과를 받아오는 객체 등록하기 ( for Google Login )
    val resultLauncher:ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(),object : ActivityResultCallback<ActivityResult>{
            override fun onActivityResult(result: ActivityResult?) {
                if( result?.resultCode == RESULT_CANCELED ) {    //  ==  if(result?.resultCode == RESULT_OK)
                    Toast.makeText(this@LoginActivity, "구글로그인 실패", Toast.LENGTH_SHORT).show()

                    return
                }

               // 로그인 결과를 가져올 intent 소환
               val intent:Intent? = result?.data

               // Intent로 부터 구글 계정 정보를 가져오는 작업 객체 생성 ( == 결과 객체가 가져온 정보를 인텐트가 끄집어 옴)  해서   .result  : 결과 테이터 받기
               val account: GoogleSignInAccount = GoogleSignIn.getSignedInAccountFromIntent(intent).result

               var id :String = account.id.toString()
               var email :String = account.email ?: "" // 엘비스 연산자 : 혹시 앞의 계정의 값이 null이면 빈칸으로 해라
               Toast.makeText(this@LoginActivity, "아이디 : $id , 이메일 $email", Toast.LENGTH_SHORT).show()
               G.userAccount = UserAccount(id,email)

               // 이제 로그인 끝낫으니 메인 ㄱㄱ
               startActivity(Intent(this@LoginActivity, MainActivity::class.java))

               finish()
            }

        })





    private fun clickLoginNaver(){

        // 네이버 아이디 로그인 [네아로] - 사용자 정보를 REST API 로 받아오는 방식 ( 레트로핏 + json 으로 받아와야 함)
        // Retrofit Library 사용
        // 네이버 개발자 센터의 가이드 문서를 참고해야 함, - 지도 제외하고 나머지는 판매자 계정 사용 안해도
        // 네이버 개발자 사이트 : https://developers.naver.com/products/login/api/api.md
        // 서드파티 애플리케이션 : 걍 아무나 써도 ㄱㅊ다~

        // 0. Nid-OAuth sdk 추가
        // jdk 버전 확인 방법
        // File -> program Structure -> SDK Location : Gradle Settings 누르면 우린 JDK 11 쓰는거 확인 가능.
        // 자바 아카이브 == jar , 안드로이드용 라이브러리 문서 == aar (근데 이거 네이버꺼쓰면 해봤더니 에러남. 걍 우린 임플리먼트 ㄱㄱ)

        // 빌드도구 발전 단계 : 엔트 -> 메이븐 -> gradle
        //  groovy : 프로그래밍 언어임. 우린 이미 build.gradle 에서 사용하고 있음~ 걍 그렇다궁

        // 자 시작 !
        // 1. 로그인 초기화

        NaverIdLoginSDK.initialize(this,"gTuWZliZEClUkVY7xz6l","_poJ7J7AC_","위치챡챡챡") // clientName: 사용자가 걍 알아듣는거

        // 2.  로그인 인증하기   :  https://developers.naver.com/docs/login/android/android.md#5--%EB%A1%9C%EA%B7%B8%EC%9D%B8
        // 이렇게 하면 로그인 정보를 받는 것이 아니라 , 로그인 정보를 받기 위한 REST API의 접근 키 ( 네이버는 키== 토큰 임. )을 발급받는 것임.
        // 이 token으로 네트워크 API 를 통해 json 데이터를 받아 정보를 얻어오는 것임.
        // 결국 서버에서 json 을 가져오는 거임 !
        // 근데 이 토큰은 그때그때 30분간만 유효.
        // 정리하면 정보를 얻기 위한 키 (토큰 )만 가져오는거지 이걸로 데이터를 또 요청해야 함.
        // 일단 토큰만 받아오기
        NaverIdLoginSDK.authenticate( this, object: OAuthLoginCallback{
            override fun onError(errorCode: Int, message: String) {
                Toast.makeText(this@LoginActivity , "로그인 토큰 발급 에러남. $message", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Toast.makeText(this@LoginActivity , "로그인 토큰 발급 실패. $message", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess() {
                Toast.makeText(this@LoginActivity , "로그인 토큰 발급성공", Toast.LENGTH_SHORT).show()

                // 사용자 정보를 가져오는 REST API 의 접속토큰 (access token)을 받아오기
                val accessToken:String? = NaverIdLoginSDK.getAccessToken()
                Log.i(" 네이버 로그인 성공으로 받은 토큰 ", accessToken + "")

                // 사용자 정보를 가져오는 네트워크 작업수행 ( => Retrofit library 사용 )
                // 쭉 내려서 API 어쩌구 ㄱㄱ   요청 URI  :  https://openapi.naver.com/v1/nid/me
                //                                      - 사이트 루트 주소 : https://openapi.naver.com
                //                                        - 레트로핏 쿼리 : v1/nid/me

                //  출력 결과 객체보면,
                // 커다란 첫째 객체 : { resultcode , message, response}
                //                                            |
                //                                            ✔
                //그 안의  둘째 객체 :                        { id, nickname . . .}
                // 이 걸 보고 바로 클래스 2개 만들어야 한다고 생각 !!!

                //일단 레트로핏 헬퍼 만들기 ㄱㄱ
                //완전 줄여서 사용 ㄱㄱ
                // 원래는
                //var call = retrofit.create(RetrofitApiService::class.java).getNaverIdUserInfo()

                val retrofit = RetrofitHelper.getRetorofitInstanse("https://openapi.naver.com")
                retrofit.create(RetrofitApiService::class.java).getNaverIdUserInfo("Bearer $accessToken")
                    .enqueue(object :Callback<NaverUserInfoResponce>{ // 콜백객체 만들기
                        override fun onResponse(
                            call: Call<NaverUserInfoResponce>,
                            response: Response<NaverUserInfoResponce>
                        ) {
                            val userInfo: NaverUserInfoResponce? = response.body()
                            var id:String = userInfo?.response?.id?: ""
                            var email:String = userInfo?.response?.email?: ""
                            G.userAccount = UserAccount(id,email)
                            Toast.makeText(this@LoginActivity, "네이버 사용자 정보가져오기 성공. 이메일 $email" , Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                        override fun onFailure(call: Call<NaverUserInfoResponce>, t: Throwable) {
                            Toast.makeText(this@LoginActivity, "네이버 사용자 정보가져오기 실패 $t", Toast.LENGTH_SHORT).show()
                        }


                })




            }



        })

    }




}//Main Class