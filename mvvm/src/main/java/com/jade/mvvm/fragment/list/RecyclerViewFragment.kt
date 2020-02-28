package com.jade.mvvm.fragment.list

import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.jade.mvvm.R
import com.jade.mvvm.fragment.BaseFragment
import com.jade.mvvm.fragment.list.adapter.BasePageListAdapter
import com.jade.mvvm.fragment.viewModel.BaseRecyclerViewModel
import com.jade.mvvm.helper.Constant

/**
 * 此类用于带有[androidx.recyclerview.widget.RecyclerView]的Fragment，建议所有符合要求的Fragment均继承于此类。
 * 此类里面定义了[androidx.recyclerview.widget.RecyclerView]了，同时还包括了其相关组件，例如：Adapter、LayoutManager，
 * 子类可以自由添加其他组件，例如：ItemDecorate,ItemAnimator等。
 * 同时此类内部默认使用Google官方的Paging库用来分页加载，用户不必自己实现。
 */
abstract class RecyclerViewFragment<MODEL, T : BaseRecyclerViewModel<*, MODEL>> : BaseFragment<T>() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: BasePageListAdapter<MODEL>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager;

    /**
     * 此方法里面主要是做一些准备操作，比如说初始化View。
     * [androidx.recyclerview.widget.RecyclerView]其他的组件也可以在此方法里面初始化
     */
    override fun onPrepareView(view: View) {
        super.onPrepareView(view)
        mRecyclerView = view.findViewById(R.id.recyclerView)
        mAdapter = onCreateAdapter()
        mRecyclerView.adapter = mAdapter
        mLayoutManager = onCreateLayoutManager()
        mRecyclerView.layoutManager = mLayoutManager
    }

    /**
     * 此方法主要是准备一些需要注入的数据
     */
    @CallSuper
    override fun onPrePareExtra() {
        putExtra(Constant.LAYOUT_MANAGER, mLayoutManager)
        putExtra(Constant.RECYCLER_ADAPTER, mAdapter)
        putExtra(Constant.RECYCLER_VIEW, mRecyclerView)
    }

    /**
     * 此方法里面准备做一些LiveData的监听操作
     */
    @CallSuper
    override fun onObserve(viewModel: T) {
        viewModel.mPageListLiveData.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }

    override fun getLayoutId() = R.layout.fragment_recyclerview

    @Suppress("UNCHECKED_CAST")
    fun <R : RecyclerView.Adapter<*>> getAdapter(): R? =
        if (this::mRecyclerView.isInitialized) null else mRecyclerView.adapter as R?


    @Suppress("UNCHECKED_CAST")
    fun <R : RecyclerView.LayoutManager> getLayoutManager(): R? =
        if (this::mRecyclerView.isInitialized) null else mRecyclerView.layoutManager as R?

    protected abstract fun onCreateAdapter(): BasePageListAdapter<MODEL>

    protected abstract fun onCreateLayoutManager(): RecyclerView.LayoutManager
}