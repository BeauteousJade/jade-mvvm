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

abstract class RecyclerViewFragment<MODEL, T : BaseRecyclerViewModel<*, MODEL>> : BaseFragment<T>() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: BasePageListAdapter<MODEL>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager;

    override fun onPrepareView(view: View) {
        super.onPrepareView(view)
        mRecyclerView = view.findViewById(R.id.recyclerView)
        mAdapter = onCreateAdapter()
        mRecyclerView.adapter = mAdapter
        mLayoutManager = onCreateLayoutManager()
        mRecyclerView.layoutManager = mLayoutManager
    }

    @CallSuper
    override fun onPrePareExtra() {
        putExtra(Constant.LAYOUT_MANAGER, mLayoutManager)
        putExtra(Constant.RECYCLER_ADAPTER, mAdapter)
        putExtra(Constant.RECYCLER_VIEW, mRecyclerView)
    }

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