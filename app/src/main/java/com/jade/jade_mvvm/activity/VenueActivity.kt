package com.jade.jade_mvvm.activity

import com.jade.jade_mvvm.fragment.RecyclerViewFragmentWithKey
import com.jade.jade_mvvm.fragment.RecyclerViewFragmentWithPosition
import com.jade.mvvm.activity.BaseActivity
import com.jade.mvvm.fragment.BaseFragment
import com.jade.mvvm.helper.delegate.ExtraDelegate
import java.lang.IllegalArgumentException

class VenueActivity : BaseActivity() {

    companion object {
        const val KEY_TYPE = "KEY_TYPE"
        const val LOAD_DATA_WITH_POSITION = 1
        const val LOAD_DATA_WITH_KEY = 2;
    }

    private val mType by ExtraDelegate(KEY_TYPE, 0)


    override fun buildCurrentFragment(): BaseFragment<*> {
        when (mType) {
            LOAD_DATA_WITH_POSITION -> return RecyclerViewFragmentWithPosition.newInstance()
            LOAD_DATA_WITH_KEY -> return RecyclerViewFragmentWithKey.newInstance()
            else ->
                throw IllegalArgumentException()
        }
    }
}