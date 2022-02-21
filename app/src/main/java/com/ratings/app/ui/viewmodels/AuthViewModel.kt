package com.ratings.app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ratings.app.AdminLoginMutation
import com.ratings.app.di.RatingsApplication
import com.ratings.app.repository.AuthRepository
import com.ratings.app.repository.HomeRepository
import com.ratings.app.repository.NetworkState
import com.ratings.app.type.CreateAdminInput
import com.ratings.app.type.CreateUserInput
import com.ratings.app.type.LoginInput
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val networkState: LiveData<NetworkState> by lazy {
        authRepository.getAuthUserNetworkState()
    }

    fun login(loginInput: LoginInput):LiveData<String> {
        return authRepository.authenticateUser(loginInput, compositeDisposable)
    }

    fun adminLogin(adminLoginInput: CreateAdminInput):LiveData<String> {
        return authRepository.authenticateAdmin(adminLoginInput, compositeDisposable)
    }

    fun saveAccessToken(accessToken: String) {
        authRepository.saveAccessToken(accessToken)
    }

    fun registerUser(createUserInput: CreateUserInput):LiveData<String> {
        return authRepository.registerUser(createUserInput, compositeDisposable)
    }

    fun signout():LiveData<String> {
        return authRepository.signout()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}