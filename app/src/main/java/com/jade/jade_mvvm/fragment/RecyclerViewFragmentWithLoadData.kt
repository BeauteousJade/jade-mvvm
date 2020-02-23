package com.jade.jade_mvvm.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jade.jade_mvvm.adapter.LoadDataAdapter
import com.jade.jade_mvvm.bean.Message
import com.jade.jade_mvvm.viewModel.RecyclerViewLoadDataViewModel
import com.jade.mvvm.fragment.refresh.RefreshRecyclerViewFragment

class RecyclerViewFragmentWithLoadData : RefreshRecyclerViewFragment<Message, RecyclerViewLoadDataViewModel>() {

    override fun onCreateAdapter() = LoadDataAdapter().apply {
        mCurrentFragment = this@RecyclerViewFragmentWithLoadData
    }

    override fun onCreateLayoutManager() = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

    override fun onCreateViewModel(): RecyclerViewLoadDataViewModel = ViewModelProvider(this).get(RecyclerViewLoadDataViewModel::class.java)

    companion object {
        fun newInstance() = RecyclerViewFragmentWithLoadData()
    }
}