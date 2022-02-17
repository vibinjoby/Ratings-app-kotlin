package com.ratings.app.helper

import android.view.View
import android.widget.ProgressBar
import com.afollestad.vvalidator.field.FieldError
import com.ratings.app.repository.NetworkState
import com.ratings.app.repository.Status
import com.ratings.app.type.UserType

fun isErrors(errors: List<FieldError>): Boolean = errors.isNotEmpty()


fun toggleProgressBarOnNetworkState(it: Any, progressBar: ProgressBar) {
    when(it) {
        NetworkState.LOADING, Status.RUNNING -> progressBar.visibility = View.VISIBLE
        else -> progressBar.visibility = View.GONE
    }
}

fun fetchUserType(userType: String): UserType {
    return when(userType) {
        "Admin" -> UserType.admin
        "User" -> UserType.customer
        "Owner" -> UserType.owner
        else -> UserType.UNKNOWN__
    }
}