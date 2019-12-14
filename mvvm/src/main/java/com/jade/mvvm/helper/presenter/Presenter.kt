package com.jade.mvvm.helper.presenter

import android.view.View
import androidx.annotation.IntDef
import androidx.lifecycle.ViewModel
import com.blade.inject.Blade
import com.jade.mvvm.activity.BaseActivity
import com.jade.mvvm.fragment.BaseFragment

open class Presenter : Presence {

    @State
    private var mState = INIT
    private var mPresence: Presence? = null
    private val mPresenterList = ArrayList<Presenter>()

    /**
     * create生命周期的回调
     * 2种case:
     * 1. mState = INIT,执行顺序：init -> create。
     * 2. mState为其他状态，非法情况。
     *
     * @param presence
     */
    fun create(presence: Presence) {
        return when (mState) {
            INIT -> {
                mPresence = presence
                mState = INIT
                onCreate()
                traverseChildPresenter {
                    it.create(presence)
                }
            }
            else -> errorState(mState, CREATE)

        }
    }

    /**
     * bind 生命周期的回调
     * 3种case：
     * 1. mState = CREATE,执行顺序：create -> bind。
     * 2. mState = BIND,执行顺序：bind -> unBind ->bind。
     * 3. mState为其他状态，非法情况。
     *
     * @param source
     * @param extra
     */
    fun bind(source: Any?, extra: Map<String, Any>?) {
        when (mState) {
            CREATE,
            BIND,
            UN_BIND -> {
                if (mState == BIND) {
                    unBind()
                }
                mState = BIND
                Blade.inject(this, source, extra)
                onBind()
                traverseChildPresenter {
                    it.bind(source, extra)
                }
            }
            else -> errorState(mState, BIND)
        }
    }

    /**
     * unBind生命周期的回调
     * 2种case：
     * 1. mState = BIND,执行顺序：bind -> unBind。
     * 2. mState为其他状态，非法情况。
     * 私有方法，不允许外部调用。
     */
    fun unBind() {
        when (mState) {
            BIND -> {
                mState = UN_BIND
                onUnBind()
                traverseChildPresenter(Presenter::unBind)
            }
            else -> errorState(mState, UN_BIND)
        }
    }

    /**
     * destroy生命周期的回调
     * 4种case：
     * 1. mState = CREATE, 执行顺序：create -> destroy。
     * 2. mState = BIND,执行顺序：bind -> unBind -> destroy；
     * 3. mState = unBind,执行顺序：unBind -> destroy。
     * 4. mState为其他状态，非法情况。
     */
    fun destroy() {
        when (mState) {
            CREATE, UN_BIND -> {
                mState = DESTROY
                onDestroy()
                traverseChildPresenter(Presenter::destroy)
            }
            BIND -> {
                unBind()
                destroy()
            }
            else -> errorState(mState, UN_BIND)
        }
    }

    protected open fun onCreate() {}

    protected open fun onBind() {}

    protected open fun onUnBind() {}

    protected open fun onDestroy() {}

    fun addPresenter(presenter: Presenter) = mPresenterList.add(presenter)

    private fun traverseChildPresenter(block: (presenter: Presenter) -> Unit) {
        mPresenterList.forEach {
            block(it)
        }
    }

    override fun <T : BaseActivity> getCurrentActivity() = mPresence?.getCurrentActivity() as T?

    override fun getRootView() = mPresence?.getRootView() as View

    override fun <T : BaseFragment<*>> getCurrentFragment() = mPresence?.getCurrentFragment() as T?

    companion object {

        @IntDef(INIT, CREATE, BIND, UN_BIND, DESTROY)
        annotation class State

        const val INIT = 0
        const val CREATE = 1
        const val BIND = 2
        const val UN_BIND = 3
        const val DESTROY = 4

        fun errorState(currentState: Int, targetState: Int): Nothing =
            throw  IllegalStateException("Don't move the current state(${currentState}) to target state(${targetState})")
    }
}