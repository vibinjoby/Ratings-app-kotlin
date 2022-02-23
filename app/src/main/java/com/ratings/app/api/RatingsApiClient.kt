package com.ratings.app.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.network.okHttpClient
import com.apollographql.apollo3.rx2.Rx2Apollo
import com.ratings.app.*
import com.ratings.app.helper.API_URL
import com.ratings.app.type.*
import io.reactivex.Single
import okhttp3.OkHttpClient
import javax.inject.Inject

class RatingsApiClient @Inject constructor(private val okHttpClient: OkHttpClient) {
    private fun getClient(): ApolloClient {
       return ApolloClient.Builder()
            .serverUrl(API_URL)
           .okHttpClient(okHttpClient)
            .build()
    }

    fun loginUser(loginInput: LoginInput): Single<ApolloResponse<LoginMutation.Data>> {
        val loginMutation = LoginMutation(loginInput)
        val apolloCall = getClient().mutation(loginMutation)
        return Rx2Apollo.flowable(apolloCall).firstOrError()
    }

    fun adminLogin(adminInput: CreateAdminInput): Single<ApolloResponse<AdminLoginMutation.Data>> {
        val adminLoginMutation = AdminLoginMutation(adminInput)
        val apolloCall = getClient().mutation(adminLoginMutation)
        return Rx2Apollo.flowable(apolloCall).firstOrError()
    }

    fun registerUser(createUserInput: CreateUserInput): Single<ApolloResponse<RegisterMutation.Data>> {
        val registerMutation = RegisterMutation(createUserInput)
        val apolloCall = getClient().mutation(registerMutation)
        return Rx2Apollo.flowable(apolloCall).firstOrError()
    }

    fun fetchOwnedRestaurants(): Single<ApolloResponse<MyRestaurantsQuery.Data>> {
        val apolloCall = getClient().query(MyRestaurantsQuery())
        return Rx2Apollo.flowable(apolloCall).firstOrError()
    }

    fun fetchAllRestaurants(first: Optional<Double>, offset: Optional<Double>): Single<ApolloResponse<RestaurantListQuery.Data>> {
        val restaurantListQuery = RestaurantListQuery(first,offset)
        val apolloCall = getClient().query(restaurantListQuery)
        return Rx2Apollo.flowable(apolloCall).firstOrError()
    }

    fun saveRestaurant(createRestaurantInput: CreateRestaurantInput): Single<ApolloResponse<CreateRestaurantMutation.Data>> {
        val createRestaurantMutation = CreateRestaurantMutation(createRestaurantInput)
        val apolloCall = getClient().mutation(createRestaurantMutation)
        return Rx2Apollo.flowable(apolloCall).firstOrError()
    }

    fun getRestaurantDetails(id: Int): Single<ApolloResponse<RestaurantDetailsQuery.Data>> {
        val query = RestaurantDetailsQuery(id)
        val apolloCall = getClient().query(query)
        return Rx2Apollo.flowable(apolloCall).firstOrError()
    }
}
