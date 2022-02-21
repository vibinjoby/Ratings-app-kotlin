package com.ratings.app.repository

import androidx.lifecycle.LiveData
import com.ratings.app.MyRestaurantsQuery
import com.ratings.app.RestaurantListQuery
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomeRepository @Inject constructor(private val homeNetworkSource: HomeNetworkSource) {

    fun fetchRestaurants(compositeDisposable: CompositeDisposable): LiveData<RestaurantListQuery.GetRestaurants> {
        homeNetworkSource.fetchAllRestaurants(compositeDisposable)
        return homeNetworkSource.restaurantList
    }

    fun getHomeNetworkState(): LiveData<NetworkState> {
        return homeNetworkSource.networkState
    }

    fun fetchOwnedRestaurants(compositeDisposable: CompositeDisposable): LiveData<MyRestaurantsQuery.Data> {
        homeNetworkSource.fetchOwnedRestaurants(compositeDisposable)
        return homeNetworkSource.ownedRestaurantsList
    }
}