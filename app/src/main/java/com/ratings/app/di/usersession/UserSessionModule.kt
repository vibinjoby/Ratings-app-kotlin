package com.ratings.app.di.usersession

import com.ratings.app.api.AuthorizationInterceptor
import com.ratings.app.type.LoginInput
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class UserSessionModule(private val accessToken: String) {

    @PerUserSession
    @Provides
    fun provideOkHttpClient(interceptor: AuthorizationInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @PerUserSession
    @Provides
    fun provideAuthorizationInterceptor(): AuthorizationInterceptor {
        return AuthorizationInterceptor(accessToken)
    }
}