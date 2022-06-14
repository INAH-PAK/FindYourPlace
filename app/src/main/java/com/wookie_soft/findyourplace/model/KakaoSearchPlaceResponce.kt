package com.wookie_soft.findyourplace.model

data class KakaoSearchPlaceResponce( var meta:PlaceMeta , var documents:MutableList<Place> )

data class PlaceMeta( var totla_count:Int , var pageable_count : Int , var is_end : Boolean)
data class Place(
    var id : String,
    var place_name : String,
    var category_name:String,
    var phone : String,
    var address_name : String,
    var road_address_name : String,
    var x : String,   // 주의 !!  longitude
    var y : String,  //          latitude
    var place_url : String,
    var distance : String // 내 좌표 까지의 거리 (단위 : meter) - 단, x,y 좌표를 준 경우에만 옴
    )