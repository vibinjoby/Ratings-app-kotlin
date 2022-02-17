package com.ratings.app.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo3.api.Optional
import com.ratings.app.RestaurantListQuery
import com.ratings.app.api.RatingsApiClient
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import javax.inject.Inject

class HomeNetworkSource @Inject constructor(private val apiService: RatingsApiClient) {
    private val TAG = "HomeNetworkSource"

    private var _restaurantList =  MutableLiveData<RestaurantListQuery.GetRestaurants>()
    val restaurantList: LiveData<RestaurantListQuery.GetRestaurants>
        get() = _restaurantList

    private var _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    fun fetchAllRestaurants(compositeDisposable: CompositeDisposable) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.fetchAllRestaurants(Optional.Absent, Optional.Absent)
                    .subscribeOn(Schedulers.io())
                    .subscribe (
                        {
                            _networkState.postValue(NetworkState.LOADED)
                            if(it.errors?.isNotEmpty() == true) {
                                _networkState.postValue(NetworkState(Status.FAILED, it.errors!![0].message))
                            } else _restaurantList.postValue(it.data?.getRestaurants)
                        },{
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e(TAG, it.toString())
                        }
                    )
            )
        } catch (e: Exception) {
            _networkState.postValue(NetworkState.ERROR)
            Log.e(TAG, e.toString())
        }
    }
}