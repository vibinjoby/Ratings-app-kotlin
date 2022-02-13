package com.ratings.app.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ratings.app.R
import com.ratings.app.api.AuthorizationInterceptor
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.helper.KEY_ACCESS_TOKEN
import com.ratings.app.helper.RATINGS_APP_SETTINGS
import com.ratings.app.repository.HomeRepository
import okhttp3.OkHttpClient

class HomeFragment : Fragment() {
    private val recyclerAdapter = RestaurantListAdapter()
    private lateinit var preferences: SharedPreferences
    private lateinit var apiService: RatingsApiClient
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeRepository: HomeRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        preferences = context?.getSharedPreferences(RATINGS_APP_SETTINGS,Context.MODE_PRIVATE)!!

        val recyclerView = view.findViewById<RecyclerView>(R.id.restaurant_list_rv)
        // Navigate to login fragment if access token doesn't exist
        if(!checkIfKeyExists()) {
            val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            findNavController().navigate(action)
        } else {
            val token = preferences.getString(KEY_ACCESS_TOKEN, "")!!

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthorizationInterceptor(token))
                .build()

            apiService = RatingsApiClient(okHttpClient)

            homeRepository = HomeRepository(apiService)
            homeViewModel = ViewModelProvider(this, object: ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return HomeViewModel(homeRepository) as T
                }
            })[HomeViewModel::class.java]

            homeViewModel.restaurantList.observe(viewLifecycleOwner, {
                val list = it?.edges?.map { edge ->
                    edge.node
                }
                if (list != null) {
                    println(list.size)
                }
                recyclerAdapter.submitList(list)
            })
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = recyclerAdapter
        }
        return view
    }

    private fun checkIfKeyExists() : Boolean {
        val preferences = requireContext().getSharedPreferences(RATINGS_APP_SETTINGS, Context.MODE_PRIVATE)
        return preferences.getString(KEY_ACCESS_TOKEN, "")?.isNotBlank() ?: false
    }
}