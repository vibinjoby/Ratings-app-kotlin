package com.ratings.app.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.type.CreateAdminInput
import com.ratings.app.type.CreateUserInput
import com.ratings.app.type.LoginInput
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import javax.inject.Inject

class AuthNetworkSource @Inject constructor(private val apiService: RatingsApiClient) {
    interface LoginCallBack {
        fun onSuccess(token: String?);
        fun onError(message: String?)
    }

    private val TAG = "AuthNetworkSource"

    private var _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private fun onFailure(it: Throwable) {
        _networkState.postValue(NetworkState.ERROR)
        Log.e(TAG, it.toString())
    }

    fun fetchAccessToken(loginInput: LoginInput, compositeDisposable: CompositeDisposable, loginCallBack: LoginCallBack) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.loginUser(loginInput)
                    .subscribeOn(Schedulers.io())
                    .subscribe ({
                        _networkState.postValue(NetworkState.LOADED)
                        if(it.errors?.isNotEmpty() == true) {
                            _networkState.postValue(NetworkState(Status.FAILED, it.errors!![0].message ))
                            loginCallBack.onError(it.errors!![0].message)
                        } else loginCallBack.onSuccess(it.data?.login?.token)
                    },{ onFailure(it) })
            )
        } catch (e: Exception) {
            _networkState.postValue(NetworkState.ERROR)
            Log.e(TAG, e.toString())
        }
    }

    fun createUser(createUserInput: CreateUserInput, compositeDisposable: CompositeDisposable, loginCallBack: AuthNetworkSource.LoginCallBack) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.registerUser(createUserInput)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            {
                                _networkState.postValue(NetworkState.LOADED)
                                if(it.errors?.isNotEmpty() == true) {
                                    loginCallBack.onError(it.errors!![0].message)
                                } else loginCallBack.onSuccess(it.data?.createUser?.token)
                            }, { onFailure(it) }
                    )
            )
        } catch (e: Exception) {
            _networkState.postValue(NetworkState.ERROR)
            Log.e(TAG, e.toString())
        }
    }

    fun fetchAccessTokenAsAdmin(loginInput: CreateAdminInput, compositeDisposable: CompositeDisposable, loginCallBack: LoginCallBack) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.adminLogin(loginInput)
                    .subscribeOn(Schedulers.io())
                    .subscribe ({
                        _networkState.postValue(NetworkState.LOADED)
                        if(it.errors?.isNotEmpty() == true) {
                            loginCallBack.onError(it.errors!![0].message)
                        } else loginCallBack.onSuccess(it.data?.loginAsAdmin?.token)
                    },{ onFailure(it) })
            )
        } catch (e: Exception) {
            _networkState.postValue(NetworkState.ERROR)
            Log.e(TAG, e.toString())
        }
    }
}