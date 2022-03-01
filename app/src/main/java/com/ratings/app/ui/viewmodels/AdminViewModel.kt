package com.ratings.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.ratings.app.repository.AdminRepository
import com.ratings.app.repository.RestaurantRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AdminViewModel @Inject constructor(repository: AdminRepository): ViewModel() {
    val compositeDisposable = CompositeDisposable()

    val usersList by lazy {
        repository.fetchAllUsers(compositeDisposable)
    }

    val restaurantsList by lazy {
        repository.fetchAllRestaurants(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}