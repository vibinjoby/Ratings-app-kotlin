package com.ratings.app.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ratings.app.R
import com.ratings.app.ui.viewmodels.AdminViewModel
import com.ratings.app.ui.viewmodels.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AllUsersFragment : DaggerFragment(R.layout.fragment_all_users) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adminViewModel: AdminViewModel by viewModels {
        viewModelFactory
    }
    private val allUsersAdapter = UserListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_users, container, false)
        val allUsersRv = view.findViewById<RecyclerView>(R.id.all_users_rv)
        allUsersRv.adapter = allUsersAdapter
        allUsersRv.layoutManager = LinearLayoutManager(requireContext())
        adminViewModel.usersList.observe(viewLifecycleOwner, {
            allUsersAdapter.submitList(it.users)
        })
        return view
    }
}