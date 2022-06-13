package com.wookie_soft.findyourplace.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayout
import com.wookie_soft.findyourplace.R
import com.wookie_soft.findyourplace.databinding.ActivityMainBinding
import com.wookie_soft.findyourplace.fragments.PlaceListFragement
import com.wookie_soft.findyourplace.fragments.PlaceMapFragement
import com.wookie_soft.findyourplace.network.RetrofitApiService
import com.wookie_soft.findyourplace.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.Callable

class MainActivity : AppCompatActivity() {

    // 1. 검색 장소 키워드
    var searchQuery:String = "화장실" // 앱 초기 검색어 -> 내 주변 개방 화장실

    //2 현재 내 위치 정보 객체 ( 위도 , 경도 정보를 맴버로 보유한 친구 )
    var myLocation: Location? = null

    //3. 구글
    val providerClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    // TODO [ Google사의  Fused Location API 사용 - play-services-locaion ]


    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 옵션 메뉴는 꼭 자바 / 코틀린 코드로 !
        //1. 툴바를 제목줄로 설정
        setSupportActionBar(binding.toolbar)
        // 옵션메뉴 만들기 -> res -> menu 폴더 생성 -> xml 만들기 -> 액티비티가 만들어지기 전에 ㅇ액션바 생성되는 메소드 : onCreateOptionsMenu

        // 처음으로 실행 될 프레그먼트를 동적으로 추가
        // 원래 프레그먼트 추가 하는 법 1. 정적 ( xml 에 아예 쓰는법) 2.지금처럼 자바/코틀린으로 쓰는 법
        // 우린 2ㄱㄱ.
        // 첫 시작으로 무조건 List 형식으로

        // 프레그먼트 작업 순서
        // beginTransaction -> 작업을 시작한다 = 수행 대기열에 올려놓는다,
        // add / delete / replace  중 하나로 작업 주고
        // commit() -> 수행 시작 !
        supportFragmentManager.beginTransaction().add(R.id.container_fragment,PlaceListFragement()).commit()

        // Tab2 클릭시, Tab2 화면에 보여져야 할 프레그먼트도 붙이자.
        binding.layoutTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) { // 탭 클릭시 불려짐
               // Tab1 -> List Layout , Tab2 -> Map Layout
                when(tab?.text){
                    "LIST" -> supportFragmentManager.beginTransaction().replace(R.id.container_fragment,PlaceListFragement()).commit()
                    "MAP"  -> supportFragmentManager.beginTransaction().replace(R.id.container_fragment,PlaceMapFragement()).commit()
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { // 원래 눌렸던 탭한테 발동됨.
                // 노필요
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
              // 노필요
            }

        }) // Tab listener

        // 검색어 입력에 따라 장소검색 요청하기
        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            searchQuery = binding.etSearch.text.toString() // 검색창에 입력된 글씨 가져옴
                // Kakao 장소 검색 API 작업 요청
            searchPlaces()

        false // -> return true 는 부모가 가진 기능을 아무것도안쓰겠다는 얘기임.
        }

        // 특정 키워드 단축 choice 버튼들에 리스너 처리하는 기능 메소드
        setChoiceButtonListener()

        // 내 위치정보 퍼미션
        val permissions:Array<String> = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        if( checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED){
            requestPermissions(permissions ,10 ) // 퍼미션 요청 다이알로그를 보이기. -> 사용자가 허용 거부 선택을 하면 , 이  override fun onRequestPermissionsResult 발동
        }else{
            Toast.makeText(this, "위치 사용를 사용 할 수 있씁니다", Toast.LENGTH_SHORT).show()
            requestMyLocation()
        }


    }// onCreate

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 10 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            requestMyLocation()
        }else{
            Toast.makeText(this, "내 위치 정보를 제공하지 않아서 검색기능을 사용할 수 없습니다", Toast.LENGTH_SHORT).show()
        }

    }

    private fun requestMyLocation(){
        //내 위치 정보얻어오는 기능 코드

        val request: LocationRequest = LocationRequest.create()
        request.interval = 1000
        request.priority = Priority.PRIORITY_HIGH_ACCURACY
        // 실시간 위치정보 갱신 요청

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        providerClient.requestLocationUpdates(request,locaionCallback, Looper.getMainLooper())
    }

    private val locaionCallback: LocationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
        super.onLocationResult(p0)
        //내 위치 정보 얻어오기
         myLocation = p0.lastLocation

        // 내 위치 정보 얻어왔으니 업뎃 그만
        providerClient.removeLocationUpdates(this) // this : 로케이션 콜백 객체 자신

        // 내 위치 정보로 타ㅏ오 검색 시작
        searchPlaces()
         }
    }



    private fun setChoiceButtonListener(){

        binding.layoutChoice.chiceWc.setOnClickListener { clickChoice(it) } // lamda식이라서, it( 누른 놈 )이 숨겨져 있음.
        binding.layoutChoice.chiceGas.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.chiceEv.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.chiceMovie.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.chiceForest.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.chiceFood.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.chice1.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.chice2.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.chice3.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.chice4.setOnClickListener { clickChoice(it) }

    }

    // 맴버변수로 기존 선택된 뷰의 아이디를 저장하는 변수
    var choiceID = R.id.chice_wc
    private fun clickChoice(view: View){
        // 1. 기존에 선택 된 뷰의 배경이미지 변경
        findViewById<ImageView>(choiceID).setBackgroundResource(R.drawable.bg_choice)
        // 2.  현재 선택됨 뷰의 배경 이미지 변경
        view.setBackgroundResource(R.drawable.bg_choice_selected)

        // 3. 현재 선택한 뷰의 id 를 맴버변수에 저장
        choiceID = view.id

        // 초이스 한 것에 따라 검색장소 키워드를 변경하여 다시 요청
        when(choiceID){
            R.id.chice_wc     ->    searchQuery = "화장실"
            R.id.chice_gas    ->    searchQuery = "주유소"
            R.id.chice_ev     ->    searchQuery = "전기차주유소"
            R.id.chice_movie  ->    searchQuery = "영화관"
            R.id.chice_forest ->    searchQuery = "공원"
            R.id.chice_food   ->    searchQuery = "맛집"
            R.id.chice_1      ->    searchQuery = "맛집"
            R.id.chice_2      ->    searchQuery = "맛집"
            R.id.chice_3      ->    searchQuery = "맛집"
            R.id.chice_4      ->    searchQuery = "맛집"
        }
        //새로운 검색 요청
        searchPlaces()
        // 검색 후, 검색창에 글씨가 있다면 지우기.
        binding.etSearch.text.clear() // 이걸 하면 포커스가 일로 가버림.
        binding.etSearch.clearFocus() // 이전 포커스로 인해 커서가 남아있을 수 도 있어서.
    }

    // Kakao 키워드 장소 검색 API 작업을 수행하는 기능메소드
    private fun searchPlaces(){
        Toast.makeText(this, "$searchQuery : ${myLocation?.latitude}", Toast.LENGTH_SHORT).show()
        // 레트로핏을 이용하여 카카오 키워드 검색 API 를 파싱하기.
        // 공식 가이드 문서 : https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-keyword
        // API 테스트 : https://developers.kakao.com/tool/rest-api/open/get/v2-local-search-keyword.%7Bformat%7D
        // 이 페이지의 응답 부분 보면, Meta : PlaceMeta
        // Parameter -> 여기서의 x 좌표 => longitude 주의.

        val retrofit : Retrofit = RetrofitHelper.getRetorofitInstanse("https://dapi.kakao.com")
        retrofit.create(RetrofitApiService::class.java)
            .searchPlacesToString(searchQuery,myLocation?.longitude.toString(),myLocation?.latitude.toString())
            .enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    var result:String? = response.body()
                    AlertDialog.Builder(this@MainActivity).setMessage("${result.toString()} 입니다.")
                        .create().show()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "서버 오류가 있습니다. + $t \n 잠시 뒤에 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            })




    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_main,menu)
        return super.onCreateOptionsMenu(menu)
    }


}// main class