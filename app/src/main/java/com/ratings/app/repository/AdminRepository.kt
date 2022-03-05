package com.ratings.app.repository

import androidx.lifecycle.LiveData
import com.ratings.app.RestaurantListQuery
import com.ratings.app.UsersListQuery
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.type.UpdateReviewInput
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AdminRepository @Inject constructor(private val adminNetworkSource: AdminNetworkSource) {

    fun fetchAllUsers(compositeDisposable: CompositeDisposable): LiveData<UsersListQuery.Data> {
        adminNetworkSource.fetchAllUsers(compositeDisposable)
        return adminNetworkSource.usersList
    }

    fun fetchAllRestaurants(compositeDisposable: CompositeDisposable): LiveData<RestaurantListQuery.GetRestaurants> {
        adminNetworkSource.fetchAllRestaurants(compositeDisposable)
        return adminNetworkSource.restaurantList
    }

    fun updateReview(compositeDisposable: CompositeDisposable, updateReviewInput: UpdateReviewInput): LiveData<NetworkState> {
        adminNetworkSource.updateReview(compositeDisposable, updateReviewInput)
        return adminNetworkSource.networkState
    }

    fun deleteUser(compositeDisposable: CompositeDisposable, userId: Int) {
        adminNetworkSource.deleteUser(compositeDisposable, userId)
    }

    fun deleteReview(compositeDisposable: CompositeDisposable, reviewId: Int): LiveData<NetworkState> {
        adminNetworkSource.deleteReview(compositeDisposable, reviewId)
        return adminNetworkSource.networkState
    }
}