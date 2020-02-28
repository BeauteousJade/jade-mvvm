package com.jade.jade_mvvm.helper.request

import com.jade.jade_mvvm.Service
import com.jade.jade_mvvm.bean.Message

class KeyMessageRequest(private val key: String, private val size: Int) : BaseRequest<List<Message>>() {
    override fun createCall() = Service.getService().getCountMessageByKey(key, size)
}