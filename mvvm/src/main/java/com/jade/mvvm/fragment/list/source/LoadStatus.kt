package com.jade.mvvm.fragment.list.source

enum class LoadStatus(val loadStatus: Int) {

    DEFAULT(0),
    LOADING(1),
    ERROR(2),
    SUCCESS(3);
}