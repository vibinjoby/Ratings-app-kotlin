package com.ratings.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ratings.app.R
import com.ratings.app.ui.viewmodels.AdminViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AllRestaurantsFragment : DaggerFragment(R.layout.fragment_all_restaurants) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adminViewModel: AdminViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar?.title = "All Restaurants"
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_restaurants, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.all_restaurants_rv)
        val restaurantListAdapter = RestaurantListAdapter {
            // navigate to review detail screen
            val action = AllRestaurantsFragmentDirections.actionAllRestaurantsFragmentToAllReviewsFragment(it.id)
            findNavController().navigate(action)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = restaurantListAdapter

        adminViewModel.restaurantsList.observe(viewLifecycleOwner, {
            val list = it?.edges?.map { edge ->
                edge.node
            }
            restaurantListAdapter.submitList(list)
        })
        return view
    }
}