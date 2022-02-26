package com.ratings.app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ratings.app.RestaurantDetailsQuery
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.type.CreateRestaurantInput
import com.ratings.app.type.CreateReviewInput
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception
import javax.inject.Inject

class RestaurantNetworkSource @Inject constructor(private val apiService: RatingsApiClient) {
    private var _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private var _restaurantDetails = MutableLiveData<RestaurantDetailsQuery.Data>()
    val restaurantDetails: LiveData<RestaurantDetailsQuery.Data>
        get() = _restaurantDetails

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

    fun getRestaurantDetails(compositeDisposable: CompositeDisposable, id: Int) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.getRestaurantDetails(id)
                    .subscribe(
                        {
                            _networkState.postValue(NetworkState.LOADED)
                            _restaurantDetails.postValue(it.data)
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

    fun createReview(compositeDisposable: CompositeDisposable, createReviewInput: CreateReviewInput) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.saveReview(createReviewInput)
                    .subscribe(
                        {
                            _networkState.postValue(NetworkState.LOADED)
                            getRestaurantDetails(compositeDisposable, createReviewInput.restaurantId)
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

    fun saveReviewReply(compositeDisposable: CompositeDisposable,restaurantId: Int, reviewId: Int, reply: String) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.saveOwnerReplyReview(reviewId, reply)
                    .subscribe(
                        {
                            _networkState.postValue(NetworkState.LOADED)
                            getRestaurantDetails(compositeDisposable, restaurantId)
                        },
                        {
                            _networkState.postValue(NetworkState(Status.FAILED, it.message!! ))
                        }
                    )
            )
        } catch (e: Exception) {

        }
    }
}