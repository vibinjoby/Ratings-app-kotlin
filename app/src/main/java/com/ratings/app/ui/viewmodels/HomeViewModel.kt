package com.ratings.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.ratings.app.repository.HomeRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val repository: HomeRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val restaurantList by lazy {
        repository.fetchRestaurants(compositeDisposable)
    }

    val ownedRestaurants by lazy {
        repository.fetchOwnedRestaurants(compositeDisposable)
    }

    val networkState by lazy {
        repository.getHomeNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}