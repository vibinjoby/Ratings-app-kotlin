package com.ratings.app.di

import com.ratings.app.di.modules.AppModule
import com.ratings.app.di.modules.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component( modules = [AndroidInjectionModule::class, AppModule::class, MainActivityModule::class])
interface AppComponent: AndroidInjector<RatingsApplication> {

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        @BindsInstance fun application(application: RatingsApplication): Builder
        fun build(): AppComponent
    }

    override fun inject(app: RatingsApplication)
}