package com.jade.mvvm.fragment.refresh

import android.content.res.Configuration
import androidx.annotation.CallSuper
import com.jade.mvvm.R
import com.jade.mvvm.fragment.list.RecyclerViewFragment
import com.jade.mvvm.fragment.viewModel.BaseRecyclerViewModel
import com.jade.mvvm.helper.presenter.Presenter

/**
 * 带下拉刷新的Fragment，继承于[com.jade.mvvm.fragment.list.RecyclerViewFragment]
 * 下拉刷新使用的是[androidx.swiperefreshlayout.widget.SwipeRefreshLayout]
 */
abstract class RefreshRecyclerViewFragment<MODEL, T : BaseRecyclerViewModel<*, MODEL>> :
    RecyclerViewFragment<MODEL, T>() {

    @CallSuper
    override fun onCreatePresenter(): Presenter {
        val presenter = Presenter()
        presenter.addPresenter(RefreshPresenter())
        return presenter
    }

    override fun getLayoutId() = R.layout.fragment_refresh_recyclerview
}