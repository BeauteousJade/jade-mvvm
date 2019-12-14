package com.jade.mvvm.network

interface RequestCallback<T> {
    fun onResult(t: T) {}

    fun onError(throwable: Throwable) {}
}