package com.wookie_soft.findyourplace.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wookie_soft.findyourplace.activities.MainActivity
import com.wookie_soft.findyourplace.activities.PlaceUrlActivity
import com.wookie_soft.findyourplace.databinding.FragmentPlaceMapBinding
import com.wookie_soft.findyourplace.model.Place
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class PlaceMapFragement : Fragment() {

    val binding : FragmentPlaceMapBinding by lazy { FragmentPlaceMapBinding.inflate(layoutInflater) }
    // layoutInflater : onCreateView 가 실행되고 만들어지는 애라 맨 첨엔 null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    val mapView:MapView by lazy { MapView(context) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.containerMapview.addView(mapView)

        // 지도 관련 설정( 자도위치 , 마커 추가 . . . )
        setMapAndMarkers()





    }

    private fun setMapAndMarkers(){

        // 마카 or 맘풍선의 클릭이벤트에 반응하는 리스너 등록
        // ** 반드시 마커 추가하는 것보다 먼저 등록되어 있어야 동작함. **
        mapView.setPOIItemEventListener(markerEventListener)

        // 지도 중심좌표 설정
        // 현재 내 위치로 이동
        var lat: Double = (activity as MainActivity).myLocation?.latitude ?: 37.5666805
        var lng: Double = (activity as MainActivity).myLocation?.longitude ?: 126.9784147


        // 위도/경도를 카카오지도의 맴좌표객체(MapPoint)로 생성
        var myMapPoint: MapPoint = MapPoint.mapPointWithGeoCoord(lat, lng)
        mapView.setMapCenterPointAndZoomLevel(myMapPoint, 5, true)
        mapView.zoomIn(true)
        mapView.zoomOut(true)

        //내 위치 마커 추가
        val marker = MapPOIItem()
        marker.apply {
            //this.itemName= "ME"
            //this.은 생략가능..
            itemName="ME"
            mapPoint= myMapPoint
            markerType= MapPOIItem.MarkerType.BluePin
            selectedMarkerType= MapPOIItem.MarkerType.YellowPin
        }
        mapView.addPOIItem(marker)

        // 검색결과 장소들 마커 추가
        val documents:MutableList<Place>? = (activity as MainActivity).searchPlaceResponce?.documents
        documents?.forEach{
            val point: MapPoint = MapPoint.mapPointWithGeoCoord(it.y.toDouble(), it.x.toDouble())

            //마커객체 생성
            var marker: MapPOIItem= MapPOIItem().apply {
                itemName= it.place_name
                mapPoint= point
                markerType= MapPOIItem.MarkerType.RedPin
                selectedMarkerType= MapPOIItem.MarkerType.YellowPin
                // 해당 POI item(마커)와 관련된 정보를 저장하고 있는 데이터객체를 보관
                userObject= it


            }
            mapView.addPOIItem(marker)
        }


    }// setMarker


    //마커나 말풍선이 클릭되는 이벤트에 반응하는 리스너객체  ////////
    private val markerEventListener:MapView.POIItemEventListener = object : MapView.POIItemEventListener{
        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
            // 마커 클릭시 발동하는 메소드
        }

        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
            // deprecated .. 이제는 아래 오버로딩된 메소드 사용 권장
        }

        override fun onCalloutBalloonOfPOIItemTouched(
            p0: MapView?,
            p1: MapPOIItem?, // 마커객체
            p2: MapPOIItem.CalloutBalloonButtonType?
        ) {
            // 마커의 말풍선을 클릭했을때 발동하는 메소드
            // 두번째 파라미터 p1 : 마커객체
            if( p1?.userObject == null ) return

            val place: Place= p1?.userObject as Place

            // 장소 상세정보 보여주는 화면으로 이동
            // 웹 뷰를 띄워주는 ->  place activity 로 !!
            val intent= Intent(context, PlaceUrlActivity::class.java)
            intent.putExtra("place_url", place.place_url)
            startActivity(intent)

        }

        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
            // 마커를 드래그하여 움직였을때 발동
        }

    }



}// Fragment class...