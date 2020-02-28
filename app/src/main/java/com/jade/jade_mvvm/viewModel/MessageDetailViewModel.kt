package com.jade.jade_mvvm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jade.jade_mvvm.bean.Message
import com.jade.jade_mvvm.helper.request.MessageDetailRequest
import com.jade.mvvm.fragment.viewModel.BaseViewModel
import com.jade.mvvm.repository.normal.BaseRepository

class MessageDetailViewModel(id: Int) : BaseViewModel<Message>(MessageDetailRepository(id)) {

    class MessageDetailRepository(private val id: Int) : BaseRepository<Message>() {
        override fun getRequest() = MessageDetailRequest(id)
    }


    class Factory(private val id: Int) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>) = MessageDetailViewModel(id) as T
    }
}
