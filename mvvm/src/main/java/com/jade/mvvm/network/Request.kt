package com.jade.mvvm.network

interface Request<T> {
    fun enqueue(requestCallback: RequestCallback<T>)
    fun cancel()
}