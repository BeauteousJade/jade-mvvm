package com.jade.jade_mvvm.presenter

import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blade.annotation.Inject
import com.jade.jade_mvvm.R
import com.jade.jade_mvvm.viewModel.MessageDetailViewModel
import com.jade.mvvm.helper.Constant
import com.jade.mvvm.helper.presenter.Presenter
import com.jade.mvvm.helper.source.helper.LoadStatus

class MessageDetailRefreshPresenter : Presenter() {

    @Inject(Constant.VIEW_MODEL)
    lateinit var mMessageDetailViewModel: MessageDetailViewModel

    private val mRefreshLayout by lazy {
        getRootView().findViewById<SwipeRefreshLayout>(R.id.refresh_layout)
    }

    override fun onBind() {
        mMessageDetailViewModel.mLoadStatusLiveData.observe(getCurrentFragment()!!, Observer<LoadStatus> {
            mRefreshLayout.isRefreshing = it == LoadStatus.LOADING_REFRESH
        })
        mRefreshLayout.setOnRefreshListener {
            mMessageDetailViewModel.refresh()
        }
        mMessageDetailViewModel.trRefresh()
    }
}