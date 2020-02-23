package com.jade.jade_mvvm.activity

import com.jade.jade_mvvm.fragment.MainFragment
import com.jade.mvvm.activity.BaseActivity

class MainActivity : BaseActivity() {
    override fun buildCurrentFragment() = MainFragment.newInstance()
}
