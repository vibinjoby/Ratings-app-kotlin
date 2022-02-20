package com.ratings.app.helper

import android.os.Build
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import com.afollestad.vvalidator.field.FieldError
import com.auth0.android.jwt.JWT
import com.ratings.app.model.UserInfo
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

@RequiresApi(Build.VERSION_CODES.O)
fun getDecodedJwt(token: String): UserInfo {
    val jwt = JWT(token)
    val jwtEntries = jwt.claims.entries
    val name = jwtEntries.find {
        it.key == "name"
    }!!.value.asString()!!
    val isAdmin = jwtEntries.find {
        it.key == "isAdmin"
    }!!.value.asBoolean()!!
    val userType = jwtEntries.find {
        it.key == "userType"
    }!!.value.asString()!!
    return UserInfo(name,isAdmin,userType )
}