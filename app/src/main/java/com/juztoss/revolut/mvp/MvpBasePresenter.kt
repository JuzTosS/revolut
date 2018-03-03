package com.juztoss.revolut.mvp

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter
import com.hannesdorfmann.mosby.mvp.MvpPresenter
import com.hannesdorfmann.mosby.mvp.MvpView

abstract class MvpBasePresenter<V : MvpView> : MvpNullObjectBasePresenter<V>() {
    abstract fun inject()
    abstract fun init()
}
