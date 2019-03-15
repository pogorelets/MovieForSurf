package ru.lostpolygons.movieapplication.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<T: BaseContract.View>: BaseContract.Presenter<T>{
    protected var disposables = CompositeDisposable()
    var view: T? = null
    var lastOperation : Runnable? = null
    operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
        disposables.add(disposable)









    }

    override fun stop() {
        disposables.clear()
    }

    override fun attach(view: T) {
        this.view = view
    }

    override fun detach(){
        view = null
    }
}