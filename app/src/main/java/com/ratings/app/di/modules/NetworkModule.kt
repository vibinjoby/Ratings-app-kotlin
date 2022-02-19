package com.ratings.app.di.modules

import android.content.SharedPreferences
import com.ratings.app.api.AuthorizationInterceptor
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.helper.KEY_ACCESS_TOKEN
import com.ratings.app.repository.AuthNetworkSource
import com.ratings.app.repository.HomeNetworkSource
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    fun provideOkHttpClient(interceptor: AuthorizationInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideAuthorizationInterceptor(preferences: SharedPreferences): AuthorizationInterceptor {
        return AuthorizationInterceptor(preferences.getString(KEY_ACCESS_TOKEN, "")?:"")
    }

    @Provides
    fun provideApiClient(okHttpClient: OkHttpClient): RatingsApiClient {
        return RatingsApiClient(okHttpClient)
    }

    @Provides
    fun provideAuthNetworkSource(apiClient: RatingsApiClient): AuthNetworkSource {
        return AuthNetworkSource(apiClient)
    }

    @Provides
    fun provideHomeNetworkSource(apiClient: RatingsApiClient): HomeNetworkSource {
        return HomeNetworkSource(apiClient)
    }
}