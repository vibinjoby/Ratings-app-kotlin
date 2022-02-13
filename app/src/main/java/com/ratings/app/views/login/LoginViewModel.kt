package com.ratings.app.views.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ratings.app.repository.AuthRepository
import com.ratings.app.repository.NetworkState
import com.ratings.app.type.LoginInput
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel(private val authRepository: AuthRepository, private val loginInput: LoginInput): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val accessToken: LiveData<String> by lazy {
        authRepository.authenticateUser(loginInput,compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        authRepository.getAuthUserNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}