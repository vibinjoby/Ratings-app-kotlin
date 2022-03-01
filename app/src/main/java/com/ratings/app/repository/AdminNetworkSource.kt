package com.ratings.app.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo3.api.Optional
import com.ratings.app.RestaurantListQuery
import com.ratings.app.UsersListQuery
import com.ratings.app.api.RatingsApiClient
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import javax.inject.Inject

class AdminNetworkSource @Inject constructor(private val apiClient: RatingsApiClient) {
    private val TAG = "AdminNetworkSource"
    private var _usersList = MutableLiveData<UsersListQuery.Data>()
    val usersList: LiveData<UsersListQuery.Data>
        get() = _usersList

    private var _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private var _restaurantList =  MutableLiveData<RestaurantListQuery.GetRestaurants>()
    val restaurantList: LiveData<RestaurantListQuery.GetRestaurants>
        get() = _restaurantList

    fun fetchAllUsers(compositeDisposable: CompositeDisposable) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiClient.getAllUsers()
                    .subscribe({
                        _networkState.postValue(NetworkState.LOADED)
                        _usersList.postValue(it.data)
                    }, {
                        _networkState.postValue(NetworkState(Status.FAILED, it.message.toString()))
                    })
            )
        } catch (e: Exception) {
            _networkState.postValue(NetworkState(Status.FAILED, e.message.toString()))
        }
    }

    fun fetchAllRestaurants(compositeDisposable: CompositeDisposable) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiClient.fetchAllRestaurants(Optional.Absent, Optional.Absent)
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