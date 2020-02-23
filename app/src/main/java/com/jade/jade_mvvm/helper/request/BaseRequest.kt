package com.jade.jade_mvvm.helper.request

import com.jade.mvvm.network.Request
import com.jade.mvvm.network.RequestCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseRequest<T> : Request<T> {

    private var mCall: Call<T>? = null

    final override fun enqueue(requestCallback: RequestCallback<T>) {
        mCall = createCall()
        mCall?.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                requestCallback.onError(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.body() != null) {
                    requestCallback.onResult(response.body()!!)
                } else {
                    requestCallback.onError(RequestException(response.errorBody()?.string()))
                }
            }

        })
    }

    abstract fun createCall(): Call<T>

    final override fun cancel() {
        mCall?.cancel()
    }

    class RequestException(override val message: String?) : Exception(message)
}