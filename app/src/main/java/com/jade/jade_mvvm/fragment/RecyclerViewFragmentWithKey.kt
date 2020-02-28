package com.jade.jade_mvvm.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jade.jade_mvvm.R
import com.jade.jade_mvvm.adapter.LoadDataAdapter
import com.jade.jade_mvvm.bean.Message
import com.jade.jade_mvvm.presenter.OperationDataPresenter
import com.jade.jade_mvvm.viewModel.KeyViewModel
import com.jade.mvvm.fragment.refresh.RefreshRecyclerViewFragment

class RecyclerViewFragmentWithKey : RefreshRecyclerViewFragment<Message, KeyViewModel>() {

    override fun getLayoutId() = R.layout.fragment_load_data

    override fun onCreateAdapter() = LoadDataAdapter().apply {
        mCurrentFragment = this@RecyclerViewFragmentWithKey
    }

    override fun onCreatePresenter() = super.onCreatePresenter().apply {
        addPresenter(OperationDataPresenter())
    }

    override fun onCreateLayoutManager() = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

    override fun onCreateViewModel() = ViewModelProvider(this).get(KeyViewModel::class.java)

    companion object {
        fun newInstance() = RecyclerViewFragmentWithKey()
    }
}