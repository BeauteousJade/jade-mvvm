package com.jade.mvvm.fragment.refresh

import androidx.annotation.CallSuper
import com.jade.mvvm.R
import com.jade.mvvm.fragment.list.RecyclerViewFragment
import com.jade.mvvm.fragment.list.viewModel.BaseRecyclerViewModel
import com.jade.mvvm.helper.presenter.Presenter

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