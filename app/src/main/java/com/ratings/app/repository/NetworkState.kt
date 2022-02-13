package com.ratings.app.repository

enum class Status {
    RUNNING, SUCCESS, FAILED
}

class NetworkState(val status: Status, val message: String) {

    companion object {
        val ENDOFLIST: NetworkState = NetworkState(Status.FAILED, "you have reached end of list")
        val LOADED: NetworkState = NetworkState(Status.SUCCESS,"Success")
        val LOADING = NetworkState(Status.RUNNING, "Running")
        val ERROR = NetworkState(Status.FAILED, "Something went wrong")
    }
}