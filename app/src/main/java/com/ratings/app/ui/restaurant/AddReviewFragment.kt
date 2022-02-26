package com.ratings.app.ui.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.vvalidator.util.onTextChanged
import com.ratings.app.R
import com.ratings.app.helper.toggleProgressBarOnNetworkState
import com.ratings.app.repository.NetworkState
import com.ratings.app.type.CreateReviewInput
import com.ratings.app.ui.viewmodels.RestaurantViewModel
import com.ratings.app.ui.viewmodels.ViewModelFactory
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject

class AddReviewFragment : DaggerFragment(R.layout.fragment_add_review) {

    private val args: AddReviewFragmentArgs by navArgs()
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val restaurantViewModel: RestaurantViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_review, container, false)
        val ratings = view.findViewById<RatingBar>(R.id.ratings_review)
        val reviewEt = view.findViewById<EditText>(R.id.review_et)
        val saveReviewButton = view.findViewById<AppCompatButton>(R.id.save_review_button)

        reviewEt.onTextChanged {
            saveReviewButton.isEnabled = it.length > 5 && ratings.rating > 0
        }

        ratings.setOnRatingBarChangeListener { rating, _, _ ->
            saveReviewButton.isEnabled = reviewEt.text.length > 5 && rating.rating > 0
        }

        saveReviewButton.setOnClickListener {
            val createReviewInput = CreateReviewInput(ratings.rating.toDouble(), Date().toString(), reviewEt.text.toString(), args.id)
            restaurantViewModel.saveReview(createReviewInput)
        }

        restaurantViewModel.networkState.observe(viewLifecycleOwner, {
            if(it == NetworkState.LOADED) {
                findNavController().navigateUp()
            }
        })
        return view
    }
}