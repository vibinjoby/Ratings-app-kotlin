package com.ratings.app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo3.api.Optional
import com.ratings.app.RestaurantDetailsQuery
import com.ratings.app.repository.AdminRepository
import com.ratings.app.repository.NetworkState
import com.ratings.app.repository.RestaurantRepository
import com.ratings.app.type.UpdateReviewInput
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import java.util.stream.Stream
import javax.inject.Inject

class AdminViewModel @Inject constructor(private val repository: AdminRepository, private val restaurantRepository: RestaurantRepository): ViewModel() {
    val compositeDisposable = CompositeDisposable()

    val networkState by lazy {
        restaurantRepository.getNetworkState()
    }

    val usersList by lazy {
        repository.fetchAllUsers(compositeDisposable)
    }

    val restaurantsList by lazy {
        repository.fetchAllRestaurants(compositeDisposable)
    }

    fun deleteUser(userId: Int) {
        repository.deleteUser(compositeDisposable, userId)
    }

    fun getAllReviews(restaurantId: Int): LiveData<RestaurantDetailsQuery.Data> {
        return restaurantRepository.getRestaurantDetails(compositeDisposable, restaurantId)
    }

    fun updateReview(updateReviewInput: UpdateReviewInput): LiveData<NetworkState> {
        return repository.updateReview(compositeDisposable, updateReviewInput)
    }

    fun fetchRestaurantDetails(restaurantId: Int) {
        restaurantRepository.getRestaurantDetails(compositeDisposable, restaurantId)
    }

    fun deleteReview(reviewId: Int): LiveData<NetworkState> {
        return repository.deleteReview(compositeDisposable, reviewId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}