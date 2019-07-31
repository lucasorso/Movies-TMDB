package br.com.lucasorso.movies.ui

import androidx.annotation.CallSuper

abstract class BasePresenter<MyContract : IContract> : IPresenter {

    protected var mViewContract: MyContract? = null

    fun setContract(view: MyContract) {
        mViewContract = view
    }

    protected fun getView(): MyContract? {
        return mViewContract
    }

    override fun resume() {}

    override fun pause() {}

    @CallSuper
    override fun destroy() {
        mViewContract = null
    }

}
