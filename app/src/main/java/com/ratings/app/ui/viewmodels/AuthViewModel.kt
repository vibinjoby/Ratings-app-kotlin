package com.ratings.app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ratings.app.AdminLoginMutation
import com.ratings.app.di.RatingsApplication
import com.ratings.app.repository.AuthNetworkSource
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
    private var _userToken =  MutableLiveData<String>()
    val userToken: LiveData<String>
        get() = _userToken

    fun login(loginInput: LoginInput) {
        authRepository.authenticateUser(loginInput, compositeDisposable, object: AuthNetworkSource.LoginCallBack{
            override fun onSuccess(token: String?) {
                token?.let {
                    authRepository.saveAccessToken(token)
                    _userToken.postValue(token)
                }
            }

            override fun onError(message: String?) {

            }

        })
    }

    fun adminLogin(adminLoginInput: CreateAdminInput) {
        authRepository.authenticateAdmin(adminLoginInput, compositeDisposable, object: AuthNetworkSource.LoginCallBack{
            override fun onSuccess(token: String?) {
                token?.let {
                    authRepository.saveAccessToken(token)
                    _userToken.postValue(token)
                }
            }

            override fun onError(message: String?) {

            }

        })
    }

    fun registerUser(createUserInput: CreateUserInput) {
        authRepository.registerUser(createUserInput, compositeDisposable, object: AuthNetworkSource.LoginCallBack{
            override fun onSuccess(token: String?) {
                token?.let {
                    authRepository.saveAccessToken(token)
                    _userToken.postValue(token)
                }
            }

            override fun onError(message: String?) {

            }

        })
    }

    fun signout() {
        authRepository.signout()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}