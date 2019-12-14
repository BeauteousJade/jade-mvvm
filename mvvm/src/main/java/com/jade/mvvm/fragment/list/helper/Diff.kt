package com.jade.mvvm.fragment.list.helper

interface Diff {
    fun areItemsTheSame(diff: Diff): Boolean
    fun onContentTheme(diff: Diff): Boolean
}