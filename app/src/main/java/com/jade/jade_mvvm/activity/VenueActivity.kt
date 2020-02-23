package com.jade.jade_mvvm.activity

import com.jade.jade_mvvm.fragment.RecyclerViewFragmentWithLoadData
import com.jade.mvvm.activity.BaseActivity
import com.jade.mvvm.fragment.BaseFragment
import com.jade.mvvm.helper.delegate.ExtraDelegate
import java.lang.IllegalArgumentException

class VenueActivity : BaseActivity() {

    companion object {
        const val KEY_TYPE = "KEY_TYPE"
        const val TYPE_RECYCLER_VIEW_LOAD_DATA = 1
    }

    private val mType by ExtraDelegate(KEY_TYPE, 0)


    override fun buildCurrentFragment(): BaseFragment<*> {
        when (mType) {
            TYPE_RECYCLER_VIEW_LOAD_DATA ->
                return RecyclerViewFragmentWithLoadData.newInstance()
            else ->
                throw IllegalArgumentException()
        }
    }
}