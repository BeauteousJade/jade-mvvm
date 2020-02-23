package com.jade.mvvm.activity

import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.jade.mvvm.R
import com.jade.mvvm.fragment.BaseFragment
import com.jade.mvvm.helper.delegate.BackPressDelete
import com.jade.mvvm.helper.delegate.OnActivityResultDelegate
import com.jade.mvvm.listener.BackPressable
import com.jade.mvvm.listener.OnActivityResultListener

abstract class BaseActivity : AppCompatActivity() {

    private val mBackPressDelegate: BackPressDelete = BackPressDelete()
    private val mOnActivityResultDelete: OnActivityResultDelegate = OnActivityResultDelegate()
    lateinit var mCurrentFragment: BaseFragment<*>
    @LayoutRes
    protected open var mContentLayoutRes = R.layout.activity_base
    @IdRes
    protected open var mContainerIdRes = R.id.fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mContentLayoutRes)
        onPrepare()
        if (savedInstanceState == null) {
            replaceFragment(buildCurrentFragment().apply {
                buildFragmentArguments()?.let {
                    arguments = it
                }
            }, mContainerIdRes)
        }
    }

    protected open fun onPrepare() {
    }

    private fun replaceFragment(fragment: BaseFragment<*>, @IdRes containerId: Int) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        fragment.arguments
        beginTransaction.replace(containerId, fragment)
        beginTransaction.commitAllowingStateLoss()
        if (this::mCurrentFragment.isInitialized) {
            mBackPressDelegate.removeBackPressable(mCurrentFragment)
        }
        mCurrentFragment = fragment
        mBackPressDelegate.addBackPressable(mCurrentFragment, 0)
    }

    final override fun onBackPressed() {
        if (!mBackPressDelegate.onBackPress()) {
            super.onBackPressed()
        }
    }

    final override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!mOnActivityResultDelete.onResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun addOnActivityResultListener(onActivityResultListener: OnActivityResultListener) =
        mOnActivityResultDelete.addOnActivityResultListener(onActivityResultListener)

    fun removeOnActivityResultListener(onActivityResultListener: OnActivityResultListener) =
        mOnActivityResultDelete.removeOnActivityResultListener(onActivityResultListener)

    fun addBackPressable(backPressable: BackPressable) = mBackPressDelegate.addBackPressable(backPressable)

    fun removeBackPressable(backPressable: BackPressable) = mBackPressDelegate.removeBackPressable(backPressable)

    protected abstract fun buildCurrentFragment(): BaseFragment<*>

    protected open fun buildFragmentArguments(): Bundle? = null
}