package com.wookie_soft.findyourplace.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wookie_soft.findyourplace.databinding.FragmentPlaceMapBinding

class PlaceMapFragement : Fragment() {

    val binding : FragmentPlaceMapBinding by lazy { FragmentPlaceMapBinding.inflate(layoutInflater) }
    // layoutInflater : onCreateView 가 실행되고 만들어지는 애라 맨 첨엔 null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
        //return super.onCreateView(inflater, container, savedInstanceState)
    }





}