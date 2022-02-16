package com.ratings.app.di.modules

import android.content.SharedPreferences
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.repository.AuthNetworkSource
import com.ratings.app.repository.AuthRepository
import com.ratings.app.repository.HomeNetworkSource
import com.ratings.app.repository.HomeRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideAuthRepository(authNetworkSource: AuthNetworkSource, preferences: SharedPreferences): AuthRepository {
        return AuthRepository(preferences, authNetworkSource )
    }

    @Singleton
    @Provides
    fun provideHomeRepository(homeNetworkSource: HomeNetworkSource) : HomeRepository {
        return HomeRepository(homeNetworkSource)
    }
 }