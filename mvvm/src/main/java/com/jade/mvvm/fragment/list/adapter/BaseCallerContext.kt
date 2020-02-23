package com.jade.mvvm.fragment.list.adapter

import androidx.fragment.app.Fragment
import com.blade.annotation.Module
import com.blade.annotation.Provides
import com.jade.mvvm.helper.Constant

@Module
class BaseCallerContext {
    @Provides(Constant.ITEM_MODEL)
    @JvmField
    var mModel: Any? = null
    @Provides(Constant.ITEM_POSITION)
    @JvmField
    var mPosition: Int = 0
    @Provides(Constant.FRAGMENT)
    @JvmField
    var mFragment: Fragment? = null
}