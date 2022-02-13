package com.ratings.app.ui.home

import androidx.lifecycle.ViewModel
import com.ratings.app.repository.HomeRepository
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(private val repository: HomeRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val restaurantList by lazy {
        repository.fetchRestaurants(compositeDisposable)
    }

    val networkState by lazy {
        repository.getHomeNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}