package com.jade.jade_mvvm.presenter

import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.blade.annotation.Inject
import com.bumptech.glide.Glide
import com.jade.jade_mvvm.R
import com.jade.jade_mvvm.activity.MessageDetailActivity
import com.jade.jade_mvvm.bean.Message
import com.jade.mvvm.activity.BaseActivity
import com.jade.mvvm.fragment.BaseFragment
import com.jade.mvvm.helper.Constant
import com.jade.mvvm.helper.presenter.Presenter

class LoadDataPresenter : Presenter() {

    @Inject(Constant.ITEM_MODEL)
    lateinit var mMessage: Message

    private val mIconView by lazy {
        getRootView().findViewById<ImageView>(R.id.icon)
    }
    private val mTitleView by lazy {
        getRootView().findViewById<TextView>(R.id.title)
    }
    private val mSummaryView by lazy {
        getRootView().findViewById<TextView>(R.id.summary)
    }

    override fun onBind() {
        Glide.with(getCurrentFragment<BaseFragment<*>>()!!)
            .asDrawable()
            .load(mMessage.icon)
            .centerCrop()
            .into(mIconView)
        mTitleView.text = mMessage.title
        mSummaryView.text = mMessage.summary

        getRootView().setOnClickListener {
            getCurrentActivity<BaseActivity>()?.startActivity(Intent(getCurrentActivity(), MessageDetailActivity::class.java).apply {
                putExtra(MessageDetailActivity.MESSAGE_ID, mMessage.id)
            })
        }
    }
}