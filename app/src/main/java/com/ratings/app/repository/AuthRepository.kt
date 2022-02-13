package com.ratings.app.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.helper.KEY_ACCESS_TOKEN
import com.ratings.app.type.LoginInput
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class AuthRepository(private val apiService: RatingsApiClient, private val context: Context) {
    private lateinit var authNetworkSource: AuthNetworkSource
    private val preferences = context.getSharedPreferences("ACCESS_TOKEN",Context.MODE_PRIVATE)

    fun authenticateUser(loginInput: LoginInput, compositeDisposable: CompositeDisposable): LiveData<String> {
        authNetworkSource = AuthNetworkSource(apiService, compositeDisposable)
        authNetworkSource.fetchAccessToken(loginInput)
        return authNetworkSource.userToken
    }

    fun saveAccessToken(accessToken: String) {
        preferences.edit().putString(KEY_ACCESS_TOKEN, accessToken)
    }

    fun getAuthUserNetworkState(): LiveData<NetworkState> {
        return authNetworkSource.networkState
    }
}