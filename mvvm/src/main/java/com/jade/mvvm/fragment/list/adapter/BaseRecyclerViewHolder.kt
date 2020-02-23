package com.jade.mvvm.fragment.list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jade.mvvm.activity.BaseActivity
import com.jade.mvvm.fragment.BaseFragment
import com.jade.mvvm.helper.presenter.Presence
import com.jade.mvvm.helper.presenter.Presenter

class BaseRecyclerViewHolder(
    itemView: View,
    presenter: Presenter,
    fragment: BaseFragment<*>?,
    extra: MutableMap<String, Any>
) : RecyclerView.ViewHolder(itemView) {
    val mPresenter = presenter
    val mPresence = object : Presence {
        @Suppress("UNCHECKED_CAST")
        override fun <T : BaseFragment<*>> getCurrentFragment() = fragment as T?

        @Suppress("UNCHECKED_CAST")
        override fun <T : BaseActivity> getCurrentActivity() = fragment?.activity as T?

        override fun getRootView() = itemView
    }
    val mExtra = extra
}