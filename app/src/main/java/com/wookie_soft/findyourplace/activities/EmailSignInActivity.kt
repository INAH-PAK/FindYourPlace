package com.wookie_soft.findyourplace.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.wookie_soft.findyourplace.G
import com.wookie_soft.findyourplace.R
import com.wookie_soft.findyourplace.databinding.ActivityEmailSignInBinding
import com.wookie_soft.findyourplace.model.UserAccount

// 이메일 로그인
class EmailSignInActivity : AppCompatActivity() {

    val binding:ActivityEmailSignInBinding by lazy { ActivityEmailSignInBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 1. 툴바를 제목줄로 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // 위에서 만든 액션바를 불러온건데 널 일 수 있으니 ?.
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 홈버튼을 뒤로가기로 하게따따
       supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24) // 내가 만든 아이코능로 설정


        //2.
        binding.btnSignin.setOnClickListener { clickSignIn() }


    }

    override fun onSupportNavigateUp(): Boolean { // 사용자가 업 버튼을 눌렀을 때
        onBackPressed() // 업버튼 눌렀을 때 뒤로가기 기능 을 실현하겠다.
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() { // 이거랑 피니쉬는 아예 다름.
        super.onBackPressed()
    }

    private fun clickSignIn(){

        // 사용자가 입력한 아이디 , 비번 가져오기
        var email:String = binding.etEmail.text.toString()
        var pw:String = binding.etPassword.text.toString()

        //Firebase FireStore DB 에서 이메일 로그인 확인 ( 아이디, 비번 확인하기.)
        // 사이트 : https://console.firebase.google.com/project/findyourlocation-a8b17/firestore/data/~2FemailUsers~2F13xOJHSZkTC7FYnKGVnq
            // 1. 데이터를 가지고 있는 디비객체 생성
            val db:FirebaseFirestore = FirebaseFirestore.getInstance() // 얘는 현재 컬렉션들을 보고 있음. 여기서 우린
            db.collection("emailUsers")//emailUsers 칸의
                .whereEqualTo( "email", email  ) // 필드가 email칸의 요기 사용자가 입력한 email 값이 있냐
                .whereEqualTo("password" , pw)
                .get().addOnSuccessListener {
                    // 데이터를 달라고 할 때 그 순간을 찰카닥 ! 찍고, 이 안에는 문서별로  쿼리 스냅샷 - 도큐먼트 스냅샷 여러개가 들어있음
                    // 디비는 중복된 데이터를 가질 수 있잖아?
                    if(it.documents.size > 0 ){ // 스냅샷아 니 안의 결과가 하나 이상이면 == 디비에 회원정보가 이씀

                        // 우리가 앱을 사용하면서, 이메일을 식별자로 하기루 하자~ 어..근데 이메일은 다른 앱 로그인에서 선택적으로 받는거라필수가 아니잖아?
                        // 기본적으로 네이ㅓ, 카카오 , 구글 등 기본적으로 무조건 로그인 하면 아이디 받음.
                        // 사용자가 거부할 수 없고, 100% id 줌.
                        // 우리는 이메일 식별자로  랜덤하게 만들어진 document를 식별자로 쓰자 !
                        // == FireStore DB 의 랜덤한 document를 id로 쓰장
                            // 이 랜덤한 도큐먼트 값은 원래 id라고 불림.
                        var id:String = it.documents[0].id

                        // 이 로그인 정보를 가진 객체를 만들어서 내 앱 내에서 글로벌하게 쓰자 !
                        G.userAccount = UserAccount(id,email)
                        G.loginType = G.EMAIL

                        val intent:Intent = Intent(this,MainActivity::class.java)
                        // 액티비티는 완전 개별 단위라서 여기서 다른 액티비티 못 건들임.
                        // 안드로이드의 액티비티는 스택 구조임. 이 하나의 스택 구조는 주머니임. new Task ==> 바구니 만드는고임.
                        // 우리 현재 상황은 . . . 로그인 하기를 누르면 메인으로 가는데 그 전의 LoginActivity가 살아있잖아?
                        // 이걸 task로 관리해야 함.
                        // 기존 task의 모든 액티비티를 제거하고 새로운 task로 시작해라 !



                    // -----------------------------------------------------TASK-------------------------------------------------------------
                    // 런처용 엑티비티가 아닌 엑티비티들을 실행하고 싶을 때, 테스트 사용. -> 자료구조의 스택구조 (바구니 ) 생각해,
                    // 화면에 상단 푸시 메세지가 뜨면 새로 액티비티를 띄우고 싶을때. . .

                    // FLAG_ACTIVITY_REORDER_TO_FRONT) -> 스택의 밑에 깔린 이미 만들어진 애를 맨 위로 올린다는 설정
                    //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)

                    // SINGLE_TOP -> 알람, 푸시메세지 할때 , 채팅 화면을 보고 있을 때 푸시로 알람뜨면 똑같은 채팅 화면을 가야 할 때, 똑같은 화면을 다시 띄우지 말라는 설정.
                    // intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    // -----------------------------------------------------------------------------------------------------------------------
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                        startActivity(intent)

                    }else{
                        // 회원 정보가 디비에 없음. == 로그인 실패
                        AlertDialog.Builder(this).setMessage("이메일과 비밀번호를 다시 확인해주세여.").create().show()
                        // 근데 이 이메일 없다는건 다시 써야한다는거잖아?
                        binding.etEmail.requestFocus()
                        binding.etEmail.selectAll()
                    }
                }
    }
}