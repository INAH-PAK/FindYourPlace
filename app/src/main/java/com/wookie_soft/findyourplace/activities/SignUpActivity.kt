package com.wookie_soft.findyourplace.activities

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.wookie_soft.findyourplace.R
import com.wookie_soft.findyourplace.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    val binding: ActivitySignUpBinding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 1. 먼저, 제목줄의 툴바를 액션바로 대체.
        // 근데 우기사 원래 겟셋 하면서 불렀잖아?
        setSupportActionBar( binding.toolbar) // 얘가 제목줄로 대신 됨. -> 액션바에 제목글씨가 표시됨
        // 2. 액션바에 제목글씨 제거
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // 3. 홈버튼 자리를 뒤로가기 버튼으로 하겠다.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)

        binding.btnSignup.setOnClickListener { clickSignUp()}

    } //onCreate

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // finish() 도 상관 없긴 함. 알아서 알아보기.
        return super.onSupportNavigateUp()
    }

    private  fun clickSignUp(){
        // Firebase FireStore DB 에 사용자 정보 저장

        var email:String = binding.etEmail.text.toString()
        var password:String = binding.etPassword.text.toString()
        var passwordConfirm:String = binding.etPasswordConfirm.text.toString()

        // 정보 보내기 전에 유효성검 사 하자.
        //        코튼인데서는 문자열 비교 시, equals() 댜신에   ==  을 사용하는 것을 권장
        // 오퍼레이터 오버라이딩 : 콘캣, 나중에 더 공부하기. 이퀄스를하면 이 콘캣이 불료짐
        if(password != passwordConfirm) {
            AlertDialog.Builder(this@SignUpActivity).setMessage("패스워드 확인에 문제가 있습니다. 다시 확인하여 입력해주시기 바랍니다.").create().show()
            //만약에 비번 틀리면 그 곳에 전체 선택이 자동으로 가 있잖아?
            binding.etPasswordConfirm.selectAll()
            return
        }



        // Firebase FireStore DB 관리객체 소환
        // 먼저 파이어베이스 설정작업 ㄱㄱㄱ 라이브러리 다운받고 ~~
        val db = FirebaseFirestore.getInstance() // 이러면 알아서 DB 객체 만들어서 줌

        // 혹시 만일, 이미 가입되어 있는 이메일을 사용한다면. . . 새로 가입을 불허 !!
        db.collection("emailUsers")
            .whereEqualTo("email",email)
            .get().addOnSuccessListener {
                //찾았다는 얘기가 아니라 서버작업은 했다!
                // 그래서 같은 값을 가진 도큐먼트가 여러개 일 수 있음. --> 만약 내가 서울 사는 사람을 검색하면 많이 나올 수 있잖아?
                // 그래서 찾았을 때, document랑 필드부분을 챨캌 찍는 느낌으로
                // 스냅샷 안에 얘가 찾아온 값들이 다 들어있음.
                if(it.documents.size > 0 ){   // 값이 암튼 들어있음 --> 같은 이메일이 이미 있음 !!
                    AlertDialog.Builder(this).setMessage("중복된 이메일이 이미 존재합니다. 다시 확인해주세요.").show()
                    binding.etEmail.requestFocus() // 2. 이렇게 포커스를 줘버리기.
                    binding.etEmail.selectAll() //1. 이건 원래 포커스를 가지고 있는 애 였을때만이 발동

                }else{ // 값이 아예 없음 --> 같은 이메일이 없음

                    // 저장 할 값들 ( 이메일 , 비밀번호 ) 를 저장하기 위해 HashMap 으로 만들기 ( Map방식으로 가장 대표적인 게 HashMap)
                    val user : MutableMap<String, String> = mutableMapOf() // ArrayList -> MutableList 같은...느낌
                    user.put("email",email)
                    user.put("password",password)  // 이 두 값을 파이어베이스 디비에 넣어야 함.



                    // collection 이름은 "emailUsers"

                    // emailUsers -> 이미 있으면 만들고 없으면 커서를 가리킴.
                    // 이렇게 콜렉션 만들면 식별자? 넘버값이 랜덤하게 만들어짐.
                    db.collection("emailUsers").add(user).addOnSuccessListener {
                        AlertDialog.Builder(this).setMessage("회원가입을 축하합니다 !")
                            .setPositiveButton("확인"
                            ) { p0, p1 -> finish() }.create().show()
                    }
                        .addOnFailureListener {
                            Toast.makeText(this@SignUpActivity, "회원가입에 실패하였습니다", Toast.LENGTH_SHORT).show()
                        }
                }


            }.addOnFailureListener {
                // 얘는 아예 그냥 서버, 보안 등의 사유로 안되는거임
                Toast.makeText(this, "서버 상태가 불안합니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }

        // 만약에 document를 랜덤말고 내가 주고 싶으면
       // db.collection("~~").document("내가 주고 싶은 값 모.. 1 ,2 이런거 넣어 줌").set( ) 해야한ㄴ데 랜덤값 주고




        // collection 이름은 "emailUsers"




         }




}