package com.ratings.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ratings.app.di.RatingsApplication
import com.ratings.app.repository.AuthRepository
import com.ratings.app.repository.HomeRepository
import com.ratings.app.repository.NetworkState
import com.ratings.app.type.LoginInput
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val networkState: LiveData<NetworkState> by lazy {
        authRepository.getAuthUserNetworkState()
    }

    fun login(loginInput: LoginInput):LiveData<String> {
        return authRepository.authenticateUser(loginInput, compositeDisposable)
    }

    fun saveAccessToken(accessToken: String) {
        RatingsApplication.get().login(accessToken)
        authRepository.saveAccessToken(accessToken)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}