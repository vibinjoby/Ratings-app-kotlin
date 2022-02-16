package com.ratings.app.helper

import android.view.View
import android.widget.ProgressBar
import com.afollestad.vvalidator.field.FieldError
import com.ratings.app.repository.NetworkState

fun isErrors(errors: List<FieldError>): Boolean = errors.isNotEmpty()


fun toggleProgressBarOnNetworkState(it: NetworkState, progressBar: ProgressBar) {
    when(it) {
        NetworkState.LOADING -> progressBar.visibility = View.VISIBLE
        NetworkState.LOADED, NetworkState.ERROR -> progressBar.visibility = View.GONE
    }
}