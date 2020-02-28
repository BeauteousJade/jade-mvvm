package com.jade.jade_mvvm.presenter

import android.view.View
import com.blade.annotation.Inject
import com.jade.jade_mvvm.R
import com.jade.jade_mvvm.adapter.LoadDataAdapter
import com.jade.jade_mvvm.bean.Message
import com.jade.jade_mvvm.viewModel.PositionViewModel
import com.jade.mvvm.fragment.viewModel.BaseRecyclerViewModel
import com.jade.mvvm.fragment.viewModel.BaseViewModel
import com.jade.mvvm.helper.Constant
import com.jade.mvvm.helper.presenter.Presenter

class OperationDataPresenter : Presenter() {
    @Inject(Constant.VIEW_MODEL)
    lateinit var mViewModel: BaseRecyclerViewModel<*, Message>
    @Inject(Constant.RECYCLER_ADAPTER)
    lateinit var mAdapter: LoadDataAdapter

    private var mClickIndex: Int = 0

    private val mAddButton by lazy {
        getRootView().findViewById<View>(R.id.add)
    }
    private val mRemoveButton by lazy {
        getRootView().findViewById<View>(R.id.remove)
    }
    private val mUpdateButton by lazy {
        getRootView().findViewById<View>(R.id.update)
    }

    override fun onBind() {
        mAddButton.setOnClickListener {
            mAdapter.currentList?.get(2)?.let {
                val updateMessage = Message(it.id, "我是新加的title " + (mClickIndex++), it.summary, it.icon, it.content)
                mViewModel.add(2, updateMessage)
            }
        }
        mRemoveButton.setOnClickListener {
            mViewModel.remove(0)
        }
        mUpdateButton.setOnClickListener {
            mAdapter.currentList?.get(10)?.let {
                val updateMessage = Message(it.id, "我是更新的title " + (mClickIndex++), it.summary, it.icon, it.content)
                mViewModel.update(10, updateMessage)
            }
            mAdapter.currentList?.get(2)?.let {
                val updateMessage = Message(it.id, "我是更新的title " + (mClickIndex++), it.summary, it.icon, it.content)
                mViewModel.update(2, updateMessage)
            }
        }
    }
}