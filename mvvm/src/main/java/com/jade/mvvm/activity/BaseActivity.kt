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

/**
 * 最基本的Activity，此类定义的方法均是非常通用的，有几点要求：
 * 1. 所有Activity都应该是BaseActivity的子类。
 * 2. 不允许往此类里面添加一些业务相关的逻辑。
 * [com.jade.mvvm.fragment.BaseFragment]
 */
abstract class BaseActivity : AppCompatActivity() {

    // back操作的监听list
    private val mBackPressDelegate: BackPressDelete = BackPressDelete()
    // activity返回值的监听list
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

    /**
     * 该方法里面主要做一些准备操作，比如说从Intent里面解析数据，初始化一些View
     */
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

    /**
     * 向[mOnActivityResultDelete]添加Activity的返回值监听事件
     */
    fun addOnActivityResultListener(onActivityResultListener: OnActivityResultListener) =
        mOnActivityResultDelete.addOnActivityResultListener(onActivityResultListener)

    /**
     * 从[mOnActivityResultDelete]移除Activity的返回值监听事件
     */
    fun removeOnActivityResultListener(onActivityResultListener: OnActivityResultListener) =
        mOnActivityResultDelete.removeOnActivityResultListener(onActivityResultListener)

    /**
     * 向[mBackPressDelegate]添加back的监听事件
     */
    fun addBackPressable(backPressable: BackPressable) = mBackPressDelegate.addBackPressable(backPressable)

    /**
     * 从[mBackPressDelegate]移除back的监听事件
     */
    fun removeBackPressable(backPressable: BackPressable) = mBackPressDelegate.removeBackPressable(backPressable)

    /**
     * 创建当前的Fragment
     * [mCurrentFragment]
     */
    protected abstract fun buildCurrentFragment(): BaseFragment<*>

    /**
     * 创建需要传递给当前Fragment的数据
     */
    protected open fun buildFragmentArguments(): Bundle? = null
}