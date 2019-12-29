package com.jade.mvvm.helper.source

import androidx.annotation.IntDef

class DataSourceSnapshot<MODEL> {
    val mModelList = ArrayList<MODEL>()
    @OperateState
    var mOperateState = DEFAULT

    fun isOperate() = mOperateState != DEFAULT

    companion object {
        @IntDef(
            DEFAULT,
            UPDATE,
            REMOVE,
            ADD
        )
        annotation class OperateState

        const val DEFAULT = 0
        const val UPDATE = 1
        const val REMOVE = 2
        const val ADD = 3
    }
}