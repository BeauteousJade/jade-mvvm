package com.jade.mvvm.fragment.list.helper

/**
 * 定义了一些list数组的基本操作，所有符合要求的类都可以实现
 * [com.jade.mvvm.fragment.viewModel.BaseRecyclerViewModel]
 */
interface ListOperation<MODEL> : Operation {
    fun update(position: Int, model: MODEL)
    fun remove(position: Int)
    fun remove(list: List<MODEL>)
    fun add(position: Int, model: MODEL)
    fun add(list: List<MODEL>)
}