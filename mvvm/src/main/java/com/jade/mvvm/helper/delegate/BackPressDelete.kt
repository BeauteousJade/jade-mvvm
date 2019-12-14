package com.jade.mvvm.helper.delegate

import com.jade.mvvm.listener.BackPressable

class BackPressDelete : BackPressable {

    private val mBackPressableList: MutableList<BackPressable> by lazy {
        ArrayList<BackPressable>()
    }

    override fun onBackPress(): Boolean {
        mBackPressableList.forEach { if (it.onBackPress()) return true }
        return false
    }

    fun addBackPressable(backPressable: BackPressable) = addBackPressable(backPressable, mBackPressableList.size)

    fun addBackPressable(backPressable: BackPressable, index: Int) = mBackPressableList.add(index, backPressable)

    fun removeBackPressable(backPressable: BackPressable) = mBackPressableList.remove(backPressable)
}