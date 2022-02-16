package com.ratings.app.di

import android.content.Context
import com.ratings.app.di.modules.AppModule
import com.ratings.app.di.usersession.UserSessionModule
import com.ratings.app.type.LoginInput
import com.ratings.app.type.UserType
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class RatingsApplication: DaggerApplication() {
    private var applicationInjector: AppComponent = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .application(this).build()

    companion object {
        lateinit var context: Context

        fun get(): RatingsApplication = context as RatingsApplication
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return applicationInjector
    }

    fun login(accessToken: String) {
        val module = UserSessionModule(accessToken)
        applicationInjector.createUserSessionComponent(module)
    }
}