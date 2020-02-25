package com.jade.jade_mvvm.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jade.jade_mvvm.R
import com.jade.jade_mvvm.adapter.LoadDataAdapter
import com.jade.jade_mvvm.bean.Message
import com.jade.jade_mvvm.presenter.OperationDataPresenter
import com.jade.jade_mvvm.viewModel.PositionViewModel
import com.jade.mvvm.fragment.refresh.RefreshRecyclerViewFragment

class RecyclerViewFragmentWithPosition :
    RefreshRecyclerViewFragment<Message, PositionViewModel>() {

    override fun getLayoutId() = R.layout.fragment_load_data

    override fun onCreateAdapter() = LoadDataAdapter().apply {
        mCurrentFragment = this@RecyclerViewFragmentWithPosition
    }

    override fun onCreatePresenter() = super.onCreatePresenter().apply {
        addPresenter(OperationDataPresenter())
    }

    override fun onCreateLayoutManager() =
        LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

    override fun onCreateViewModel(): PositionViewModel =
        ViewModelProvider(this).get(PositionViewModel::class.java)

    companion object {
        fun newInstance() = RecyclerViewFragmentWithPosition()
    }
}