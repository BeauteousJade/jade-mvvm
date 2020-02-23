package com.jade.jade_mvvm.helper.retrofit

import androidx.lifecycle.LiveData
import com.jade.mvvm.network.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapter<R>(private val responseType: Type) : CallAdapter<R, LiveData<ApiResponse<R>>> {
    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {
        return object : LiveData<ApiResponse<R>>() {
            private val started = AtomicBoolean(false)
            override fun onActive() {
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onFailure(call: Call<R>, t: Throwable) {
                            postValue(ApiResponse(null, t))
                        }

                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            postValue(ApiResponse(response.body(), null))
                        }
                    })
                }
            }
        }
    }

    override fun responseType() = responseType
}