package com.ratings.app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ratings.app.RestaurantDetailsQuery
import com.ratings.app.repository.NetworkState
import com.ratings.app.repository.RestaurantRepository
import com.ratings.app.type.CreateRestaurantInput
import com.ratings.app.type.CreateReviewInput
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RestaurantViewModel @Inject constructor(private val repository: RestaurantRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val networkState: LiveData<NetworkState> by lazy {
        repository.getNetworkState()
    }

    fun saveRestaurant(createRestaurantInput: CreateRestaurantInput) {
        repository.saveRestaurant(compositeDisposable,createRestaurantInput)
    }

    fun getRestaurantDetails(id: Int): LiveData<RestaurantDetailsQuery.Data> {
        return repository.getRestaurantDetails(compositeDisposable, id)
    }

    fun saveReview(createReviewInput: CreateReviewInput) {
        repository.createReview(compositeDisposable,createReviewInput)
    }

    fun saveOwnerResponse(restaurantId: Int, reviewId: Int, reply: String) {
        repository.saveOwnerReply(compositeDisposable, restaurantId, reviewId, reply)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}