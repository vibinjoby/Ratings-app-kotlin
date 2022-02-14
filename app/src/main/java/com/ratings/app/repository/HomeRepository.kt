package com.ratings.app.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ratings.app.RestaurantListQuery
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.helper.KEY_ACCESS_TOKEN
import com.ratings.app.helper.RATINGS_APP_SETTINGS
import com.ratings.app.type.LoginInput
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiService: RatingsApiClient) {
    private lateinit var homeNetworkSource: HomeNetworkSource

    fun fetchRestaurants(compositeDisposable: CompositeDisposable): LiveData<RestaurantListQuery.GetRestaurants> {
        homeNetworkSource = HomeNetworkSource(apiService, compositeDisposable)
        homeNetworkSource.fetchAllRestaurants()
        return homeNetworkSource.restaurantList
    }

    fun getHomeNetworkState(): LiveData<NetworkState> {
        return homeNetworkSource.networkState
    }
}