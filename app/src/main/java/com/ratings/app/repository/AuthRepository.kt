package com.ratings.app.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.helper.KEY_ACCESS_TOKEN
import com.ratings.app.type.CreateUserInput
import com.ratings.app.type.LoginInput
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AuthRepository @Inject constructor(private val preferences: SharedPreferences,private val authNetworkSource: AuthNetworkSource) {

    fun authenticateUser(loginInput: LoginInput, compositeDisposable: CompositeDisposable): LiveData<String> {
        authNetworkSource.fetchAccessToken(loginInput, compositeDisposable)
        return authNetworkSource.userToken
    }

    fun saveAccessToken(accessToken: String) {
        preferences.edit().putString(KEY_ACCESS_TOKEN, accessToken).commit()
    }

    fun registerUser(createUserInput: CreateUserInput, compositeDisposable: CompositeDisposable): LiveData<String> {
        authNetworkSource.createUser(createUserInput, compositeDisposable )
        return authNetworkSource.userToken
    }

    fun getAuthUserNetworkState(): LiveData<NetworkState> {
        return authNetworkSource.networkState
    }
}