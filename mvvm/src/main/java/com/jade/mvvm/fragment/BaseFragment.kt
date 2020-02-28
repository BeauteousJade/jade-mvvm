package com.jade.mvvm.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.blade.annotation.Module
import com.blade.annotation.Provides
import com.jade.mvvm.activity.BaseActivity
import com.jade.mvvm.helper.Constant
import com.jade.mvvm.helper.delegate.BackPressDelete
import com.jade.mvvm.helper.delegate.OnActivityResultDelegate
import com.jade.mvvm.helper.presenter.Presence
import com.jade.mvvm.helper.presenter.Presenter
import com.jade.mvvm.listener.BackPressable
import com.jade.mvvm.listener.OnActivityResultListener

/**
 * 最基本的Fragment，此类同[com.jade.mvvm.activity.BaseActivity]一样，定义了一些Fragment通用的方法。
 * 所有的Fragment必须直接或者间接继承于它。
 * [com.jade.mvvm.activity.BaseActivity]
 */
abstract class BaseFragment<T : ViewModel> : Fragment(), BackPressable, OnActivityResultListener, Presence {

    private val mBackPressDelete = BackPressDelete()
    private val mOnActivityResultDelegate = OnActivityResultDelegate()
    private val mViewModel: T? by lazy { onCreateViewModel() }
    private var mPresenter: Presenter? = null
    private val mExtras = HashMap<String, Any>()

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onPrepareView(view)
        mViewModel?.apply {
            onObserve(this)
        }
        initPresenter()
    }

    private fun initPresenter() {
        onPrePareExtra()
        val baseCallerContext = BaseCallerContext()
        mViewModel?.apply {
            baseCallerContext.mViewModel = this
        }

        mPresenter = onCreatePresenter()
        mPresenter?.create(this)
        mPresenter?.bind(baseCallerContext, mExtras)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.destroy()
    }

    override fun onBackPress(): Boolean = mBackPressDelete.onBackPress()

    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean =
        mOnActivityResultDelegate.onResult(resultCode, resultCode, data)

    fun addBackPressable(backPressable: BackPressable) = mBackPressDelete.addBackPressable(backPressable)

    fun removeBackPressable(backPressable: BackPressable) = mBackPressDelete.removeBackPressable(backPressable)

    fun addOnActivityResultListener(onActivityResultListener: OnActivityResultListener) =
        mOnActivityResultDelegate.addOnActivityResultListener(onActivityResultListener)

    fun removeOnActivityResultListener(onActivityResultListener: OnActivityResultListener) =
        mOnActivityResultDelegate.removeOnActivityResultListener(onActivityResultListener)

    protected open fun onPrepareView(view: View) {
    }

    protected open fun onPrePareExtra() {
    }

    protected fun putExtra(key: String, value: Any) = mExtras.put(key, value)

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseActivity> getCurrentActivity(): T? = activity as? T

    @Suppress("UNCHECKED_CAST")
    override fun <T : BaseFragment<*>> getCurrentFragment(): T? = this as? T

    override fun getRootView(): View = view!!

    protected open fun onObserve(viewModel: T) {}

    protected open fun onCreateViewModel(): T? = null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected open fun onCreatePresenter(): Presenter? = null

    @Module
    class BaseCallerContext {
        @Provides(value = Constant.VIEW_MODEL)
        @JvmField
        var mViewModel: ViewModel? = null
    }
}