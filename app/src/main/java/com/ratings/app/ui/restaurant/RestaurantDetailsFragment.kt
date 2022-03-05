package com.ratings.app.ui.restaurant

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ratings.app.R
import com.ratings.app.helper.KEY_ACCESS_TOKEN
import com.ratings.app.helper.RESTAURANT_IMG_URL
import com.ratings.app.helper.getDecodedJwt
import com.ratings.app.helper.toggleProgressBarOnNetworkState
import com.ratings.app.type.UserType
import com.ratings.app.ui.viewmodels.RestaurantViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class RestaurantDetailsFragment : DaggerFragment(R.layout.fragment_restaurant_details) {

    private val args: RestaurantDetailsFragmentArgs by navArgs()
    @Inject lateinit var preferences: SharedPreferences
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val restaurantViewModel: RestaurantViewModel by viewModels {
        viewModelFactory
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_restaurant_details, container, false)

        val progressBar = view.findViewById<ProgressBar>(R.id.restaurant_details_progress_bar)
        val restaurantIv = view.findViewById<ImageView>(R.id.restaurant_iv)
        val restaurantName = view.findViewById<TextView>(R.id.restaurant_name_tv)
        val addReviewBtn = view.findViewById<FloatingActionButton>(R.id.add_review_btn)
        val ratingsTv = view.findViewById<TextView>(R.id.rating_tv)
        val reviewsHeader = view.findViewById<TextView>(R.id.reviews_header_tv)
        val noReviewsLayout = view.findViewById<LinearLayout>(R.id.no_reviews_layout)
        val reviewRv = view.findViewById<RecyclerView>(R.id.reviews_rv)

        val token = preferences.getString(KEY_ACCESS_TOKEN,"")!!
        val userType = getDecodedJwt(token).userType

        addReviewBtn.setOnClickListener {
            val action = RestaurantDetailsFragmentDirections.actionRestaurantDetailsFragmentToAddReviewFragment(args.id)
            findNavController().navigate(action)
        }
        restaurantViewModel.getRestaurantDetails(args.id)
            .observe(viewLifecycleOwner, {
                val reviewAdapter = ReviewListAdapter(userType, null) { reviewId, reply ->
                    restaurantViewModel.saveOwnerResponse(args.id, reviewId,reply )
                }
                Glide.with(this)
                    .load(RESTAURANT_IMG_URL)
                    .into(restaurantIv)
                restaurantName.text = it.getRestaurant.restaurantName
                ratingsTv.text = it.getRestaurant.averageRatings.toInt().toString()

                if(it.getRestaurant.reviews?.isEmpty() == true) {
                    reviewsHeader.visibility = View.GONE
                    noReviewsLayout.visibility = View.VISIBLE
                } else {
                    reviewsHeader.visibility = View.VISIBLE
                    noReviewsLayout.visibility = View.GONE
                    reviewAdapter.submitList(it.getRestaurant.reviews)
                    reviewRv.adapter = reviewAdapter
                    reviewRv.layoutManager = LinearLayoutManager(requireContext())
                }

                if(userType == UserType.customer.toString()) {
                    addReviewBtn.visibility = View.VISIBLE
                } else {
                    addReviewBtn.visibility = View.GONE
                }
            })

        restaurantViewModel.networkState.observe(viewLifecycleOwner, {
            // Show progress bar based on network status
            toggleProgressBarOnNetworkState(it, progressBar)
        })
        return view
    }
}