package com.jade.jade_mvvm.fragment

import androidx.lifecycle.ViewModelProvider
import com.jade.jade_mvvm.R
import com.jade.jade_mvvm.activity.MessageDetailActivity
import com.jade.jade_mvvm.presenter.MessageDetailInitViewPresenter
import com.jade.jade_mvvm.presenter.MessageDetailRefreshPresenter
import com.jade.jade_mvvm.viewModel.MessageDetailViewModel
import com.jade.mvvm.fragment.BaseFragment
import com.jade.mvvm.helper.delegate.ExtraDelegate
import com.jade.mvvm.helper.presenter.Presenter

class MessageDetailFragment : BaseFragment<MessageDetailViewModel>() {

    private val mMessageId by ExtraDelegate(MessageDetailActivity.MESSAGE_ID, 0)

    override fun getLayoutId() = R.layout.fragment_message_detail

    override fun onCreateViewModel() =
        ViewModelProvider(this, MessageDetailViewModel.Factory(mMessageId))[MessageDetailViewModel::class.java]

    override fun onCreatePresenter() = Presenter().apply {
        addPresenter(MessageDetailRefreshPresenter())
        addPresenter(MessageDetailInitViewPresenter())
    }

    companion object {
        fun newInstance() = MessageDetailFragment()
    }
}