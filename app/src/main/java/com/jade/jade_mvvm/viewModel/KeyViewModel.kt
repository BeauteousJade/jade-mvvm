package com.jade.jade_mvvm.viewModel

import androidx.paging.DataSource
import com.jade.jade_mvvm.bean.Message
import com.jade.jade_mvvm.helper.request.KeyMessageRequest
import com.jade.mvvm.fragment.viewModel.BaseRecyclerViewModel
import com.jade.mvvm.helper.source.BaseDataSourceFactory
import com.jade.mvvm.helper.source.impl.BaseItemKeyedDataSource
import com.jade.mvvm.network.RequestCallback
import com.jade.mvvm.repository.list.ItemKeyedRepository

class KeyViewModel : BaseRecyclerViewModel<String, Message>(PositionDataSourceFactory()) {

    class PositionDataSourceFactory : BaseDataSourceFactory<String, Message>() {

        override fun createDataSource(): DataSource<String, Message> {
            return object : BaseItemKeyedDataSource<String, Message>(object : ItemKeyedRepository<String,
                    List<Message>> {
                override fun loadInit(key: String?, size: Int, requestCallback: RequestCallback<List<Message>>) {
                    if (key == null) {
                        KeyMessageRequest("", size).enqueue(requestCallback)
                    } else {
                        KeyMessageRequest(key, size).enqueue(requestCallback)
                    }
                }

                override fun loadAfter(key: String?, size: Int, requestCallback: RequestCallback<List<Message>>) {
                    key?.let {
                        KeyMessageRequest(it, size).enqueue(requestCallback)
                    }
                }

            }, mDataSourceSnapshot) {}
        }
    }
}