package com.ratings.app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.type.CreateRestaurantInput
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception
import javax.inject.Inject

class RestaurantNetworkSource @Inject constructor(private val apiService: RatingsApiClient,
                                                  private val homeNetworkSource: HomeNetworkSource) {
    private var _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    fun createRestaurant(compositeDisposable: CompositeDisposable, createRestaurantInput: CreateRestaurantInput) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.saveRestaurant(createRestaurantInput)
                    .subscribe(
                        {
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState(Status.FAILED, it.message!! ))
                        }
                )
            )
        } catch (e: Exception) {
            _networkState.postValue(NetworkState(Status.FAILED, e.message!! ))
        }
    }
}