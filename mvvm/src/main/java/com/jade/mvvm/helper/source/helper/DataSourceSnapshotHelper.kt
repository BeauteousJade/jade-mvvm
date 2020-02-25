package com.jade.mvvm.helper.source.helper

import androidx.paging.DataSource
import com.jade.mvvm.fragment.list.helper.ListOperation
import com.jade.mvvm.helper.source.DataSourceSnapshot

class DataSourceSnapshotHelper<MODEL>(
    private val dataSource: DataSource<*, MODEL>,
    private val mDataSourceSnapshot: DataSourceSnapshot<MODEL>
) :
    ListOperation<MODEL> {

    fun getSnapShot() = mDataSourceSnapshot.mModelList

    fun isOperate() = mDataSourceSnapshot.isOperate()

    fun resetStatus() {
        mDataSourceSnapshot.mOperateState = DataSourceSnapshot.DEFAULT
    }

    fun recordUpdate(list: List<MODEL>) {
        mDataSourceSnapshot.mModelList.clear()
        mDataSourceSnapshot.mModelList.addAll(list)
    }

    fun recordAddLast(list: List<MODEL>) {
        mDataSourceSnapshot.mModelList.addAll(list)
    }

    fun recordAddFirst(list: List<MODEL>) {
        mDataSourceSnapshot.mModelList.addAll(0, list)
    }

    override fun remove(position: Int) {
        mDataSourceSnapshot.mOperateState =
            DataSourceSnapshot.REMOVE
        mDataSourceSnapshot.mModelList.removeAt(position)
        dataSource.invalidate()
    }

    override fun remove(list: List<MODEL>) {
        mDataSourceSnapshot.mOperateState =
            DataSourceSnapshot.REMOVE
        mDataSourceSnapshot.mModelList.removeAll(list)
        dataSource.invalidate()
    }

    override fun add(position: Int, model: MODEL) {
        mDataSourceSnapshot.mOperateState =
            DataSourceSnapshot.ADD
        mDataSourceSnapshot.mModelList.add(position, model)
        dataSource.invalidate()
    }

    override fun add(list: List<MODEL>) {
        mDataSourceSnapshot.mOperateState =
            DataSourceSnapshot.ADD
        mDataSourceSnapshot.mModelList.addAll(list)
        dataSource.invalidate()
    }

    override fun refresh() {
        mDataSourceSnapshot.mOperateState = DataSourceSnapshot.DEFAULT
        dataSource.invalidate()
    }

    override fun update(position: Int, model: MODEL) {
        mDataSourceSnapshot.mOperateState =
            DataSourceSnapshot.UPDATE
        mDataSourceSnapshot.mModelList[position] = model
        dataSource.invalidate()
    }
}