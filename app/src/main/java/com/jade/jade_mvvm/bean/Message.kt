package com.jade.jade_mvvm.bean

import com.jade.mvvm.helper.source.PageModel

data class Message(
    var id: Long, var title: String, var summary: String, var icon: String, var content: String, private var
    key: String = ""
) :
    PageModel<String> {
    constructor(id: Long, title: String, summary: String, icon: String) : this(id, title, summary, icon, "", "")

    override fun getKey() = key
}