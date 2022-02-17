package com.ratings.app.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.network.okHttpClient
import com.apollographql.apollo3.rx2.Rx2Apollo
import com.ratings.app.AdminLoginMutation
import com.ratings.app.LoginMutation
import com.ratings.app.RegisterMutation
import com.ratings.app.RestaurantListQuery
import com.ratings.app.helper.API_URL
import com.ratings.app.type.CreateAdminInput
import com.ratings.app.type.CreateUserInput
import com.ratings.app.type.LoginInput
import com.ratings.app.type.UserType
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

    fun adminLogin(username: String, password: String): Single<ApolloResponse<AdminLoginMutation.Data>> {
        val adminInput = CreateAdminInput(username,password)
        val adminLoginMutation = AdminLoginMutation(adminInput)
        val apolloCall = getClient().mutation(adminLoginMutation)
        return Rx2Apollo.flowable(apolloCall).firstOrError()
    }

    fun registerUser(createUserInput: CreateUserInput): Single<ApolloResponse<RegisterMutation.Data>> {
        val registerMutation = RegisterMutation(createUserInput)
        val apolloCall = getClient().mutation(registerMutation)
        return Rx2Apollo.flowable(apolloCall).firstOrError()
    }

    fun fetchAllRestaurants(first: Optional<Double>, offset: Optional<Double>): Single<ApolloResponse<RestaurantListQuery.Data>> {
        val restaurantListQuery = RestaurantListQuery(first,offset)
        val apolloCall = getClient().query(restaurantListQuery)
        return Rx2Apollo.flowable(apolloCall).firstOrError()
    }
}
