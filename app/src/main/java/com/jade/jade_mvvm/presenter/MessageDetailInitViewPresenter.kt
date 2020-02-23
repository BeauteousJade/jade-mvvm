package com.jade.jade_mvvm.presenter

import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.blade.annotation.Inject
import com.bumptech.glide.Glide
import com.jade.jade_mvvm.R
import com.jade.jade_mvvm.viewModel.MessageDetailViewModel
import com.jade.mvvm.fragment.BaseFragment
import com.jade.mvvm.helper.Constant
import com.jade.mvvm.helper.presenter.Presenter

class MessageDetailInitViewPresenter : Presenter() {

    @Inject(Constant.VIEW_MODEL)
    lateinit var mMessageDetailViewModel: MessageDetailViewModel

    private val mIconView by lazy {
        getRootView().findViewById<ImageView>(R.id.icon)
    }
    private val mTitleView by lazy {
        getRootView().findViewById<TextView>(R.id.title)
    }

    private val mContentView by lazy {
        getRootView().findViewById<TextView>(R.id.content)
    }

    override fun onBind() {
        mMessageDetailViewModel.mLoadDataLiveData.observe(getCurrentFragment()!!, Observer {
            Glide.with(getCurrentFragment<BaseFragment<*>>()!!)
                .asBitmap()
                .load(it.icon)
                .centerCrop()
                .into(mIconView)
            mTitleView.text = it.title
            mContentView.text = it.content
        })
    }
}