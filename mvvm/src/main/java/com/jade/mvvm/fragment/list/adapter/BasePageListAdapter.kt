package com.jade.mvvm.fragment.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
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
        holder.mPresenter.bind(onCreateCallerContext(position), holder.mExtra)

    final override fun onViewRecycled(holder: BaseRecyclerViewHolder) = holder.mPresenter.unBind()

    /**
     * 创建当前ItemView的Presenter，可以根据[viewType]来创建不同的Presenter
     */
    protected open fun onCreatePresenter(viewType: Int): Presenter = Presenter()

    /**
     * 创建当前ItemView需要注入的数据源对象，此方法区别于[putExtra]方法。
     * 此方法注入的数据是可以针对某一个View，也可以是通用的数据。
     */
    protected open fun onCreateCallerContext(position: Int): Any {
        val baseCallerContext = BaseCallerContext()
        baseCallerContext.mModel = getItem(position)
        baseCallerContext.mPosition = position
        baseCallerContext.mFragment = mCurrentFragment
        return baseCallerContext
    }

    /**
     * 向数据源put一些通用数据，不建议将一些某个ItemView可能用的数据通过此方法注入
     * [onCreateCallerContext]
     */
    fun putExtra(id: String, extra: Any) = mExtra.put(id, extra)

    /**
     * 返回当前ItemView的布局，可以根据[viewType]返回不同的布局
     */
    @LayoutRes
    protected abstract fun getItemViewLayout(viewType: Int): Int
}