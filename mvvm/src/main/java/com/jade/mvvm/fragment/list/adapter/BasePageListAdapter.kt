package com.jade.mvvm.fragment.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jade.mvvm.fragment.BaseFragment
import com.jade.mvvm.fragment.list.helper.DiffUtilCallback
import com.jade.mvvm.helper.presenter.Presenter

abstract class BasePageListAdapter<MODEL> :
    PagedListAdapter<MODEL, BaseRecyclerViewHolder>(DiffUtilCallback()) {
    private val mExtra = HashMap<String, Any>()
    var mCurrentFragment: BaseFragment<*>? = null

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(getItemViewLayout(viewType), parent, false)
        val presenter = onCreatePresenter(viewType)
        val viewHolder = BaseRecyclerViewHolder(view, presenter, mCurrentFragment, mExtra)
        presenter.create(viewHolder.mPresence)
        return viewHolder
    }

    final override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) =
        holder.mPresenter.bind(onCreateCallerContext(), holder.mExtra)

    final override fun onViewRecycled(holder: BaseRecyclerViewHolder) = holder.mPresenter.destroy()

    protected open fun onCreatePresenter(viewType: Int): Presenter = Presenter()

    protected open fun onCreateCallerContext(): Any? = null

    fun putExtra(id: String, extra: Any) = mExtra.put(id, extra)

    @LayoutRes
    protected abstract fun getItemViewLayout(viewType: Int): Int
}