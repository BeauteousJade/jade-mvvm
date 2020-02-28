package com.jade.jade_mvvm.activity

import android.os.Bundle
import com.jade.jade_mvvm.fragment.MessageDetailFragment
import com.jade.mvvm.activity.BaseActivity
import com.jade.mvvm.helper.delegate.ExtraDelegate

class MessageDetailActivity : BaseActivity() {

    private val mMessageId by ExtraDelegate(MESSAGE_ID, 0)

    override fun buildCurrentFragment() = MessageDetailFragment.newInstance()

    override fun buildFragmentArguments() = Bundle().apply {
        putInt(MESSAGE_ID, mMessageId)
    }

    companion object {
        const val MESSAGE_ID = "MESSAGE_ID"
    }
}