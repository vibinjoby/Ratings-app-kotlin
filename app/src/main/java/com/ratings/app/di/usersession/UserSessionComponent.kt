package com.ratings.app.di.usersession

import com.ratings.app.api.AuthorizationInterceptor
import com.ratings.app.di.AppComponent
import com.ratings.app.di.RatingsApplication
import com.ratings.app.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

@PerUserSession
@Subcomponent(modules = [UserSessionModule::class])
interface UserSessionComponent {
    fun getAuthorizationInterceptor(): AuthorizationInterceptor
}