package com.ratings.app.di.modules

import com.ratings.app.ui.home.HomeFragment
import com.ratings.app.ui.auth.LoginFragment
import com.ratings.app.ui.auth.SignupFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeSignupFragment(): SignupFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment
}