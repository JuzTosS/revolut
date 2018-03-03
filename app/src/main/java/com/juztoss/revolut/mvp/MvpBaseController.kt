package com.juztoss.revolut.mvp

import android.os.Bundle
import com.hannesdorfmann.mosby.mvp.MvpView
import com.hannesdorfmann.mosby.mvp.conductor.MvpController

abstract class MvpBaseController<V : MvpView, P : MvpBasePresenter<V>> : MvpController<V, P> {
    constructor()
    constructor(args: Bundle) : super(args)

    final override fun createPresenter(): P {
        val presenter = createNewPresenter()
        presenter.inject()
        presenter.init()
        return presenter
    }

    abstract fun createNewPresenter(): P
}
