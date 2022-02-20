package com.ratings.app.model

import com.ratings.app.type.UserType

data class UserInfo(
    val name: String,
    val isAdmin: Boolean,
    val userType: String
)
