package com.ratings.app.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.ratings.app.BuildConfig
import com.ratings.app.di.RatingsApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, NetworkModule::class, RepositoryModule::class])
class AppModule(private val app: RatingsApplication) {
    companion object {
        const val KEY_PREFS = "RATINGS_APP_KEY_PREFS"
    }

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = app

    @Provides
    @Singleton
    fun provideSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(KEY_PREFS + BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }
}