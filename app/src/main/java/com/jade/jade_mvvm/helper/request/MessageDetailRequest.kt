package com.jade.jade_mvvm.helper.request

import com.jade.jade_mvvm.Service
import com.jade.jade_mvvm.bean.Message

class MessageDetailRequest(private val id: Int) : BaseRequest<Message>() {
    override fun createCall() = Service.getService().getDetailMessage(id)
}