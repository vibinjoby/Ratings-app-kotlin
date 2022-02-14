package com.ratings.app.di.modules

import com.ratings.app.api.RatingsApiClient
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
}