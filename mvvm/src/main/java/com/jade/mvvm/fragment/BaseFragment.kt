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
import com.blade.annotation.Inject
import com.blade.annotation.Module
import com.blade.annotation.Provides
import com.jade.mvvm.R
import com.jade.mvvm.activity.BaseActivity
import com.jade.mvvm.helper.Constant
import com.jade.mvvm.helper.delegate.BackPressDelete
import com.jade.mvvm.helper.delegate.OnActivityResultDelegate
import com.jade.mvvm.helper.presenter.Presence
import com.jade.mvvm.helper.presenter.Presenter
import com.jade.mvvm.listener.BackPressable
import com.jade.mvvm.listener.OnActivityResultListener

abstract class BaseFragment<T : ViewModel> : Fragment(), BackPressable, OnActivityResultListener, Presence {

    private val mBackPressDelete = BackPressDelete()
    private val mOnActivityResultDelegate = OnActivityResultDelegate()
    private val mViewModel: T? by lazy { onCreateViewModel() }
    private lateinit var mPresenter: Presenter
    private val mExtras = HashMap<String, Any>()

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container)
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
        opPrePareExtra()
        val baseCallerContext = BaseCallerContext()
        baseCallerContext.mViewModel = mViewModel!!

        mPresenter = onCreatePresenter()
        mPresenter.create(this)
        mPresenter.bind(baseCallerContext, mExtras)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.destroy()
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

    @CallSuper
    protected open fun onPrepareView(view: View) {
    }

    @CallSuper
    protected open fun opPrePareExtra() {
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

    protected abstract fun onCreatePresenter(): Presenter

    @Module
    class BaseCallerContext {
        @Provides(value = Constant.VIEW_MODEL)
        lateinit var mViewModel: ViewModel
    }
}