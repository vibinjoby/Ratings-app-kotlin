package com.ratings.app.api

import com.apollographql.apollo3.ApolloClient
import com.ratings.app.helper.API_URL

object RatingsApiClient {
    fun getClient(): ApolloClient {
       return ApolloClient.Builder()
            .serverUrl(API_URL)
            .build()
    }
}
