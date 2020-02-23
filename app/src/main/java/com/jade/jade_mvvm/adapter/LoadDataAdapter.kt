package com.jade.jade_mvvm.adapter

import com.jade.jade_mvvm.R
import com.jade.jade_mvvm.bean.Message
import com.jade.jade_mvvm.presenter.LoadDataPresenter
import com.jade.mvvm.fragment.list.adapter.BasePageListAdapter

class LoadDataAdapter : BasePageListAdapter<Message>() {
    override fun getItemViewLayout(viewType: Int) = R.layout.list_item_load_data

    override fun onCreatePresenter(viewType: Int) = LoadDataPresenter()
}