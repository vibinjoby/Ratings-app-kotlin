package com.ratings.app.ui.admin

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ratings.app.R
import com.ratings.app.helper.ADMIN_HOME_LIST
import com.ratings.app.ui.customviews.AppAlertDialog
import com.ratings.app.ui.home.HomeFragmentDirections
import com.ratings.app.ui.viewmodels.AuthViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AdminHomeFragment : DaggerFragment(R.layout.fragment_admin_home) {

    private lateinit var adapter: AdminHomeListAdapter
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
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
                    val action = AdminHomeFragmentDirections.actionAdminHomeFragmentToLoginFragment()
                    findNavController().navigate(action)
                }
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as AppCompatActivity?)!!.supportActionBar?.title = "Welcome Admin"
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.admin_item_rv)
        buildRecyclerView(recyclerView)
        return view
    }

    private fun buildRecyclerView(recyclerView: RecyclerView) {
        adapter = AdminHomeListAdapter {
            if(it.title == "Users") {
                val action = AdminHomeFragmentDirections.actionAdminHomeFragmentToAllUsersFragment()
                findNavController().navigate(action)
            } else {
                val action = AdminHomeFragmentDirections.actionAdminHomeFragmentToAllRestaurantsFragment()
                findNavController().navigate(action)
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        adapter.submitList(ADMIN_HOME_LIST)
    }
}