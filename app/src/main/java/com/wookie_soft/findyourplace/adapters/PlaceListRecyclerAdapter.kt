package com.wookie_soft.findyourplace.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wookie_soft.findyourplace.R
import com.wookie_soft.findyourplace.activities.PlaceUrlActivity
import com.wookie_soft.findyourplace.databinding.RecylerItemListFragmentBinding
import com.wookie_soft.findyourplace.model.Place

class PlaceListRecyclerAdapter(val context: Context, var documents: MutableList<Place>) : RecyclerView.Adapter<PlaceListRecyclerAdapter.VH>() {


    inner class VH (itemView: View) : RecyclerView.ViewHolder(itemView){

        // 아이템 뷰 하나에 3개의 텍스트뷰가 바인딩되어있음,
        // onCreateViewHolder 로부터 넘어온 아이템 뷰가 바인딩에 붙었음
        val binding:RecylerItemListFragmentBinding = RecylerItemListFragmentBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView:View = LayoutInflater.from(context).inflate(R.layout.recyler_item_list_fragment,parent,false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        // 이제 보여지는 글씨를 내 데이터로 만들기
        val place : Place = documents[position]

        holder.binding.tvPlaceName.text = place.place_name
        if(place.road_address_name == "") holder.binding.tvAddress.text = place.address_name // 도로명 주소가 없을 때
        else holder.binding.tvAddress.text = place.road_address_name
        holder.binding.tvDistance.text = place.distance + "m"

        // 장소 아이템뷰를 클릭했을 때, 장소에 대한 상세 화면으로 이동
        holder.itemView.setOnClickListener {
            val intent:Intent = Intent(context, PlaceUrlActivity::class.java)
            intent.putExtra("placeUrl",place.place_url)
            Log.i(" 아이템 뷰 클릭시 uri ", place.place_url )
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = documents.size // 코틀린의 할당연산자 ( 리턴이 하나일 땐, 이렇게 줄일 수 있음, )

}