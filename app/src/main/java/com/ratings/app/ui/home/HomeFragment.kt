package com.ratings.app.ui.home

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ratings.app.R
import com.ratings.app.helper.KEY_ACCESS_TOKEN
import com.ratings.app.helper.getDecodedJwt
import com.ratings.app.helper.toggleProgressBarOnNetworkState
import com.ratings.app.repository.Status
import com.ratings.app.type.UserType
import com.ratings.app.ui.customviews.AppAlertDialog
import com.ratings.app.ui.viewmodels.AuthViewModel
import com.ratings.app.ui.viewmodels.HomeViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment(R.layout.fragment_home) {
    @Inject lateinit var preferences: SharedPreferences
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val homeViewModel: HomeViewModel by viewModels {
        viewModelFactory
    }
    private val authViewModel: AuthViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.logout_menu -> {
                val dialog = AppAlertDialog(this.requireContext()
                    ,"Are you sure you want to logout?")
                    {
                        authViewModel.signout()
                        val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                        findNavController().navigate(action)
                    }
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val addRestaurantBtn = view.findViewById<FloatingActionButton>(R.id.add_restaurant_btn)
        val progressBar = view.findViewById<ProgressBar>(R.id.home_progress_bar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.restaurant_list_rv)

        val token = preferences.getString(KEY_ACCESS_TOKEN, "")

        // Navigate to login fragment if access token doesn't exist
        if(token?.isNullOrBlank() == true) {
            val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            findNavController().navigate(action)
        } else {
            val userInfo = getDecodedJwt(token)
            // Admins should land in their own home page
            if(userInfo.isAdmin) {
                val action = HomeFragmentDirections.actionHomeFragmentToAdminHomeFragment()
                findNavController().navigate(action)
            }
            // Add restaurant button should be only visible for owners
            if(userInfo.userType == UserType.customer.toString()) {
                val recyclerAdapter = RestaurantListAdapter {
                    navigateToDetails(it.id)
                }
                addRestaurantBtn.visibility = View.GONE

                homeViewModel.restaurantList.observe(viewLifecycleOwner, {
                    val list = it?.edges?.map { edge ->
                        edge.node
                    }
                    recyclerAdapter.submitList(list)
                })
                recyclerView.adapter = recyclerAdapter
            } else {
                addRestaurantBtn.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToAddRestaurantFragment()
                    findNavController().navigate(action)
                }
                val recyclerAdapter = OwnedRestaurantListAdapter {
                    navigateToDetails(it.id)
                }
                recyclerView.adapter = recyclerAdapter
                homeViewModel.ownedRestaurants.observe(viewLifecycleOwner, {
                    recyclerAdapter.submitList(it.getOwnedrestaurants)
                })
            }
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            homeViewModel.networkState.observe(viewLifecycleOwner, {
                // If any errors, navigate to login screen
                if(it.status == Status.FAILED) {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                    findNavController().navigate(action)
                }
                // Show progress bar based on network status
                toggleProgressBarOnNetworkState(it, progressBar)
            })
        }
        return view
    }

    private fun navigateToDetails(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToRestaurantDetailsFragment(id)
        findNavController().navigate(action)
    }
}