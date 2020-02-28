package com.jade.mvvm.fragment.refresh

import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blade.annotation.Inject
import com.jade.mvvm.R
import com.jade.mvvm.helper.source.helper.LoadStatus
import com.jade.mvvm.fragment.viewModel.BaseRecyclerViewModel
import com.jade.mvvm.helper.Constant
import com.jade.mvvm.helper.presenter.Presenter

class RefreshPresenter : Presenter() {
    @Inject(Constant.VIEW_MODEL)
    lateinit var mViewModel: BaseRecyclerViewModel<*, *>
    private lateinit var mRefreshLayout: SwipeRefreshLayout
    private val mLoadStatusObserver = Observer<LoadStatus> {
        mRefreshLayout.isRefreshing = it == LoadStatus.LOADING_REFRESH
    }

    override fun onCreate() {
        mRefreshLayout = getRootView().findViewById(R.id.refresh_layout)
    }

    override fun onBind() {
        mViewModel.mLoadStatusLiveData.observe(getCurrentFragment()!!, mLoadStatusObserver)
        mRefreshLayout.setOnRefreshListener {
            mViewModel.refresh()
        }
    }

    override fun onUnBind() {
        mViewModel.mLoadStatusLiveData.removeObserver(mLoadStatusObserver)
    }
}