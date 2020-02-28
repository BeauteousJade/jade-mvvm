package com.jade.mvvm.fragment.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.jade.mvvm.fragment.list.helper.Operation
import com.jade.mvvm.repository.normal.Repository

/**
 * 普通Fragment的ViewModel，此ViewModel支持刷新刷新，
 * 刷新的具体细节可以通过实现[com.jade.mvvm.repository.normal.Repository]而达到目的。
 */
abstract class BaseViewModel<MODEL>(val repository: Repository<MODEL>) : ViewModel(),
    Operation {

    /**
     * 加载状态的LiveData
     * [com.jade.mvvm.helper.source.helper.LoadStatus]
     */
    val mLoadStatusLiveData = switchMap(repository.getLoadStatusLiveData()) {
        MutableLiveData(it)
    }
    // 加载数据的LiveData
    val mLoadDataLiveData = switchMap(repository.getLoadDataLiveData()) {
        MutableLiveData(it)
    }

    /**
     * 尝试刷新。[mLoadDataLiveData]已经含有数据，不会刷新。
     * 此方法使用于Fragment创建之后刷新数据，因为Fragment创建之后，可能会因为Configuration(例如屏幕旋转)的改变导致Fragment
     * 的重建，此时调用此方法是最合适的，因为不会真正的刷新，而是去加载[mLoadDataLiveData]已有的数据。如此，便节省了没必要的操作，
     * 同时也会节省用户的流量。
     * [refresh]
     */
    fun trRefresh() {
        if (mLoadDataLiveData.value == null) {
            refresh()
        }
    }

    /**
     * 强制刷新。
     * 区别于[trRefresh]
     */
    override fun refresh() = repository.load()
}