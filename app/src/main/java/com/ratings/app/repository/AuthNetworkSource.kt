package com.ratings.app.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.type.LoginInput
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import javax.inject.Inject

class AuthNetworkSource @Inject constructor(private val apiService: RatingsApiClient) {
    private val TAG = "AuthNetworkSource"
    private var _userToken =  MutableLiveData<String>()
    val userToken: LiveData<String>
        get() = _userToken

    private var _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    fun fetchAccessToken(loginInput: LoginInput, compositeDisposable: CompositeDisposable) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.loginUser(loginInput)
                    .subscribeOn(Schedulers.io())
                    .subscribe (
                        {
                            _networkState.postValue(NetworkState.LOADED)
                            _userToken.postValue(it.data?.login?.token)
                        },{
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e(TAG, it.toString())
                        }
                    )
            )
        } catch (e: Exception) {
            _networkState.postValue(NetworkState.ERROR)
            Log.e(TAG, e.toString())
        }
    }
}