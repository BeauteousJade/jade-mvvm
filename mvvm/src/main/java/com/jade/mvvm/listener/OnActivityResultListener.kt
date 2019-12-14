package com.jade.mvvm.listener

import android.content.Intent

interface OnActivityResultListener {
    fun onResult(requestCode: Int, resultCode: Int, data: Intent?):Boolean
}