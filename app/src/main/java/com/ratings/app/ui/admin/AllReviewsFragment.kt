package com.ratings.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo3.api.Optional
import com.ratings.app.R
import com.ratings.app.helper.toggleProgressBarOnNetworkState
import com.ratings.app.repository.NetworkState
import com.ratings.app.type.UpdateReviewInput
import com.ratings.app.ui.restaurant.ReviewListAdapter
import com.ratings.app.ui.viewmodels.AdminViewModel
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject

class AllReviewsFragment : DaggerFragment(R.layout.fragment_all_reviews) {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adminViewModel: AdminViewModel by viewModels {
        viewModelFactory
    }
    private val args: AllReviewsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar?.title = "All Reviews"
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_reviews, container, false)
        val progressBar = view.findViewById<ProgressBar>(R.id.all_reviews_progress_bar)
        val allReviewsRv = view.findViewById<RecyclerView>(R.id.all_reviews_rv)
        val reviewListAdapter = ReviewListAdapter("admin", { reviewText, ratingBar, ownerResponse,reviewId ->
            val updateReviewInput = UpdateReviewInput(ratingBar, Date().toString(), reviewText,Optional.presentIfNotNull(args.id),reviewId, ownerResponse)
            adminViewModel.updateReview(updateReviewInput).observe(viewLifecycleOwner, {
                if(it == NetworkState.LOADED) {
                    adminViewModel.fetchRestaurantDetails(args.id)
                }
            })
        })
        allReviewsRv.layoutManager = LinearLayoutManager(requireContext())
        allReviewsRv.adapter = reviewListAdapter

        adminViewModel.getAllReviews(args.id).observe(viewLifecycleOwner, {
            reviewListAdapter.submitList(it.getRestaurant.reviews)
        })

        adminViewModel.networkState.observe(viewLifecycleOwner, {
            toggleProgressBarOnNetworkState(it, progressBar)
        })

        return view
    }
}