package com.ratings.app.repository

import androidx.lifecycle.LiveData
import com.ratings.app.RestaurantDetailsQuery
import com.ratings.app.type.CreateRestaurantInput
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RestaurantRepository @Inject constructor(private val networkSource: RestaurantNetworkSource) {
    fun getNetworkState(): LiveData<NetworkState> {
        return networkSource.networkState
    }

    fun saveRestaurant(compositeDisposable: CompositeDisposable, createRestaurantInput: CreateRestaurantInput) {
        networkSource.createRestaurant(compositeDisposable, createRestaurantInput)
    }

    fun getRestaurantDetails(compositeDisposable: CompositeDisposable, id: Int): LiveData<RestaurantDetailsQuery.Data> {
        networkSource.getRestaurantDetails(compositeDisposable, id)
        return networkSource.restaurantDetails
    }
}