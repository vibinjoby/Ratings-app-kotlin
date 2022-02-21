package com.ratings.app.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ratings.app.R
import dagger.android.support.DaggerFragment

class AllRestaurantsFragment : DaggerFragment(R.layout.fragment_all_restaurants) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_restaurants, container, false)
    }
}