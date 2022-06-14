package com.wookie_soft.findyourplace.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wookie_soft.findyourplace.activities.MainActivity
import com.wookie_soft.findyourplace.adapters.PlaceListRecyclerAdapter
import com.wookie_soft.findyourplace.databinding.FragmentPlaceListBinding

class PlaceListFragement : Fragment() {

    val binding : FragmentPlaceListBinding by lazy { FragmentPlaceListBinding.inflate(layoutInflater) }
    // layoutInflater : onCreateView 가 실행되고 만들어지는 애라 맨 첨엔 null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPlaceListRecylerAdapter()


    }

    private fun setPlaceListRecylerAdapter(){ //뷰 설정 작업은 메소드로 !
        val ma : MainActivity = activity as MainActivity // 메인을 가져옴,

        // 아직 MainActivity 가 파싱작업이 완료되지 않았다면 -> 데이터가 없어서 docments 가 null !!!
        if(ma.searchPlaceResponce == null) return // 이렇게 하면 여기서 메소드가 끝남.

        binding.recycler.adapter =PlaceListRecyclerAdapter(requireContext(), ma.searchPlaceResponce!!.documents)

    }

}