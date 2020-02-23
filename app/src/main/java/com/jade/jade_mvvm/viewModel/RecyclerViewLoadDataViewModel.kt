package com.jade.jade_mvvm.viewModel

import com.jade.jade_mvvm.bean.Message
import com.jade.jade_mvvm.helper.request.MessageRequest
import com.jade.mvvm.fragment.viewModel.BaseRecyclerViewModel
import com.jade.mvvm.helper.source.BaseDataSourceFactory
import com.jade.mvvm.helper.source.impl.BasePositionDataSource
import com.jade.mvvm.network.RequestCallback
import com.jade.mvvm.repository.list.ListPositionRepository

class RecyclerViewLoadDataViewModel : BaseRecyclerViewModel<Int, Message>(LoadDataDataSourceFactory()) {

    class LoadDataDataSourceFactory : BaseDataSourceFactory<Int, Message>() {
        override fun createDataSource() = object : BasePositionDataSource<Message>(object : ListPositionRepository<List<Message>> {

            override fun loadMore(startPosition: Int, loadSize: Int, requestCallback: RequestCallback<List<Message>>) =
                MessageRequest(startPosition, loadSize).enqueue(requestCallback)


            override fun loadInit(size: Int, requestCallback: RequestCallback<List<Message>>) =
                MessageRequest(0, size).enqueue(requestCallback)
        }) {}
    }
}