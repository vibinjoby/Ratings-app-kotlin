package com.ratings.app.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.helper.KEY_ACCESS_TOKEN
import com.ratings.app.type.CreateAdminInput
import com.ratings.app.type.CreateUserInput
import com.ratings.app.type.LoginInput
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AuthRepository @Inject constructor(private val preferences: SharedPreferences,private val authNetworkSource: AuthNetworkSource) {

    fun authenticateUser(loginInput: LoginInput, compositeDisposable: CompositeDisposable, loginCallBack: AuthNetworkSource.LoginCallBack) {
        authNetworkSource.fetchAccessToken(loginInput, compositeDisposable, loginCallBack)
    }

    fun authenticateAdmin(loginInput: CreateAdminInput, compositeDisposable: CompositeDisposable, loginCallBack: AuthNetworkSource.LoginCallBack) {
        authNetworkSource.fetchAccessTokenAsAdmin(loginInput, compositeDisposable, loginCallBack)
    }

    fun saveAccessToken(accessToken: String) {
        preferences.edit().putString(KEY_ACCESS_TOKEN, accessToken).commit()
    }

    fun registerUser(createUserInput: CreateUserInput, compositeDisposable: CompositeDisposable, loginCallBack: AuthNetworkSource.LoginCallBack) {
        authNetworkSource.createUser(createUserInput, compositeDisposable, loginCallBack )
    }

    fun getAuthUserNetworkState(): LiveData<NetworkState> {
        return authNetworkSource.networkState
    }

    fun signout() {
        preferences.edit().putString(KEY_ACCESS_TOKEN, "").commit()
    }
}