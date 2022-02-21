package com.ratings.app.helper

import com.ratings.app.R
import com.ratings.app.model.AdminHomeInfo

const val RESTAURANT_IMG_URL = "https://source.unsplash.com/user/picoftasty"
const val API_URL = "https://ratings-nest-gql.herokuapp.com/graphql"
const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
const val RATINGS_APP_SETTINGS = "RATINGS_APP_SETTINGS"

val ADMIN_HOME_LIST = listOf(AdminHomeInfo(R.drawable.admin_user_pic, "Users", "Find all the users registered in our app both customers and owners"),
    AdminHomeInfo(R.drawable.admin_restaurant_pic, "Restaurants", "Find all the restaurants created by owners in our app")
    )