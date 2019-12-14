package com.jade.mvvm.helper.presenter

import android.view.View
import com.jade.mvvm.activity.BaseActivity
import com.jade.mvvm.fragment.BaseFragment

interface Presence {
    fun <T : BaseFragment<*>> getCurrentFragment(): T?

    fun <T : BaseActivity> getCurrentActivity(): T?

    fun getRootView(): View
}