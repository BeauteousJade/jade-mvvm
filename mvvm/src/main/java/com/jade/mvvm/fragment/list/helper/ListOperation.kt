package com.jade.mvvm.fragment.list.helper

interface ListOperation<MODEL> : Operation {
    fun update(position: Int, model: MODEL)
    fun remove(position: Int)
    fun remove(list: List<MODEL>)
    fun add(position: Int, model: MODEL)
    fun add(list: List<MODEL>)
}