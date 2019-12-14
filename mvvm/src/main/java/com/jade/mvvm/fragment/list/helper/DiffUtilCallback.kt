package com.jade.mvvm.fragment.list.helper

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback<MODEL> : DiffUtil.ItemCallback<MODEL>() {
    override fun areItemsTheSame(oldItem: MODEL, newItem: MODEL) =
        if (oldItem is Diff && newItem is Diff) oldItem.areItemsTheSame(newItem) else oldItem!! == newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: MODEL, newItem: MODEL) =
        if (oldItem is Diff && newItem is Diff) oldItem.onContentTheme(newItem) else oldItem!! == newItem
}