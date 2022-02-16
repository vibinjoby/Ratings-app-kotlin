package com.ratings.app.di.modules

import com.ratings.app.api.RatingsApiClient
import com.ratings.app.repository.AuthNetworkSource
import com.ratings.app.repository.HomeNetworkSource
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideApiClient(okHttpClient: OkHttpClient): RatingsApiClient {
        return RatingsApiClient(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideAuthNetworkSource(apiClient: RatingsApiClient): AuthNetworkSource {
        return AuthNetworkSource(apiClient)
    }

    @Singleton
    @Provides
    fun provideHomeNetworkSource(apiClient: RatingsApiClient): HomeNetworkSource {
        return HomeNetworkSource(apiClient)
    }
}