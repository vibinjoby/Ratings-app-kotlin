package com.ratings.app.di.modules

import com.ratings.app.ui.admin.AdminHomeFragment
import com.ratings.app.ui.admin.AllRestaurantsFragment
import com.ratings.app.ui.admin.AllUsersFragment
import com.ratings.app.ui.home.HomeFragment
import com.ratings.app.ui.auth.LoginFragment
import com.ratings.app.ui.auth.SignupFragment
import com.ratings.app.ui.restaurant.AddRestaurantFragment
import com.ratings.app.ui.restaurant.RestaurantDetailsFragment
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

    @ContributesAndroidInjector
    abstract fun contributeAdminHomeFragment(): AdminHomeFragment

    @ContributesAndroidInjector
    abstract fun contributeAllUsersFragment(): AllUsersFragment

    @ContributesAndroidInjector
    abstract fun contributeAllRestaurantsFragment(): AllRestaurantsFragment

    @ContributesAndroidInjector
    abstract fun contributeAddRestaurantFragment(): AddRestaurantFragment

    @ContributesAndroidInjector
    abstract fun contributeRestaurantDetailsFragment(): RestaurantDetailsFragment
}