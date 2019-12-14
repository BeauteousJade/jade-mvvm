package com.jade.mvvm.helper.delegate

import android.content.Intent
import com.jade.mvvm.listener.OnActivityResultListener

class OnActivityResultDelegate : OnActivityResultListener {

    private val mOnActivityResultListenerList: MutableList<OnActivityResultListener> by lazy {
        ArrayList<OnActivityResultListener>()
    }

    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        mOnActivityResultListenerList.forEach { if (it.onResult(requestCode, resultCode, data)) return true }
        return false
    }

    fun addOnActivityResultListener(onActivityResultListener: OnActivityResultListener) =
        addOnActivityResultListener(onActivityResultListener, mOnActivityResultListenerList.size)


    fun addOnActivityResultListener(onActivityResultListener: OnActivityResultListener, index: Int) =
        mOnActivityResultListenerList.add(index, onActivityResultListener);

    fun removeOnActivityResultListener(onActivityResultListener: OnActivityResultListener) =
        mOnActivityResultListenerList.remove(onActivityResultListener)
}