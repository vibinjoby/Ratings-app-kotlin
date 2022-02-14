package com.ratings.app.di.modules

import android.content.SharedPreferences
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.repository.AuthRepository
import com.ratings.app.repository.HomeRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideAuthRepository(apiClient: RatingsApiClient, preferences: SharedPreferences): AuthRepository {
        return AuthRepository(preferences, apiClient )
    }

    @Singleton
    @Provides
    fun provideHomeRepository(apiClient: RatingsApiClient) : HomeRepository {
        return HomeRepository(apiClient)
    }
 }