package com.jade.jade_mvvm.bean

data class Message(val id: Long, val title: String, val summary: String, val icon: String, val content: String) {
    constructor(id: Long, title: String, summary: String, icon: String) : this(id, title, summary, icon, "")
}