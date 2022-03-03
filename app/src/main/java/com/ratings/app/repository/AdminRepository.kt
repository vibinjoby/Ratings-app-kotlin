package com.ratings.app.repository

import androidx.lifecycle.LiveData
import com.ratings.app.RestaurantListQuery
import com.ratings.app.UsersListQuery
import com.ratings.app.api.RatingsApiClient
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

    fun deleteUser(compositeDisposable: CompositeDisposable, userId: Int) {
        adminNetworkSource.deleteUser(compositeDisposable, userId)
    }
}