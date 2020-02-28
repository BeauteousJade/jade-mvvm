package com.jade.jade_mvvm.viewModel

import com.jade.jade_mvvm.bean.Message
import com.jade.jade_mvvm.helper.request.PositionMessageRequest
import com.jade.mvvm.fragment.viewModel.BaseRecyclerViewModel
import com.jade.mvvm.helper.source.BaseDataSourceFactory
import com.jade.mvvm.helper.source.impl.BasePositionDataSource
import com.jade.mvvm.network.RequestCallback
import com.jade.mvvm.repository.list.ListPositionRepository

class PositionViewModel : BaseRecyclerViewModel<Int, Message>(PositionDataSourceFactory()) {

    class PositionDataSourceFactory : BaseDataSourceFactory<Int, Message>() {
        override fun createDataSource() =
            object : BasePositionDataSource<Message>(object : ListPositionRepository<List<Message>> {

                override fun loadMore(
                    startPosition: Int,
                    loadSize: Int,
                    requestCallback: RequestCallback<List<Message>>
                ) =
                    PositionMessageRequest(startPosition, loadSize).enqueue(requestCallback)


                override fun loadInit(size: Int, requestCallback: RequestCallback<List<Message>>) =
                    PositionMessageRequest(0, size).enqueue(requestCallback)
            }, mDataSourceSnapshot) {}
    }
}