package com.jade.jade_mvvm.helper.request

import com.jade.jade_mvvm.Service
import com.jade.jade_mvvm.bean.Message

class PositionMessageRequest(private val position: Int, private val count: Int) : BaseRequest<List<Message>>() {
    override fun createCall() = Service.getService().getCountMessageFromPosition(position, count)
}