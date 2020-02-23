package com.jade.mvvm.helper.delegate

import com.jade.mvvm.activity.BaseActivity
import com.jade.mvvm.fragment.BaseFragment
import kotlin.reflect.KProperty

class ExtraDelegate<T>(private val extraName: String, private val defaultValue: T) {
    private var mOldExtra: T? = null

    operator fun getValue(thisRef: BaseActivity, property: KProperty<*>): T {
        mOldExtra = getExtra(mOldExtra, extraName, thisRef)
        return mOldExtra ?: defaultValue
    }

    operator fun getValue(thisRef: BaseFragment<*>, property: KProperty<*>): T {
        mOldExtra = getExtra(mOldExtra, extraName, thisRef)
        return mOldExtra ?: defaultValue
    }

    @Suppress("UNCHECKED_CAST")
    private fun getExtra(oldExtra: T?, extraName: String, thisRef: BaseActivity) = oldExtra ?: thisRef.intent?.extras?.get(extraName) as T

    @Suppress("UNCHECKED_CAST")
    private fun getExtra(oldExtra: T?, extraName: String, thisRef: BaseFragment<*>) = oldExtra ?: thisRef.arguments?.get(extraName) as T
}