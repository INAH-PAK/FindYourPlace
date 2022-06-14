package com.wookie_soft.findyourplace.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wookie_soft.findyourplace.databinding.RecylerItemListFragmentBinding
import com.wookie_soft.findyourplace.model.Place

class PlaceListRecyclerAdapter(val context: Context, var documents: MutableList<Place>) : RecyclerView.Adapter<PlaceListRecyclerAdapter.VH>() {


    inner class VH (itemView: View) : RecyclerView.ViewHolder(itemView){

        // 아이템 뷰 하나에 3개의 텍스트뷰가 바인딩되어있음,
        val binding:RecylerItemListFragmentBinding = RecylerItemListFragmentBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = documents.size // 코틀린의 할당연산자 ( 리턴이 하나일 땐, 이렇게 줄일 수 있음, )

}