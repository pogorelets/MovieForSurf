package ru.lostpolygons.movieapplication.base

interface BaseContract {
    interface View {
        fun showProgress()
        fun dismissProgress()
    }

    interface Presenter <V: BaseContract.View>{
        fun start()
        fun stop()
        fun attach(view: V)
        fun detach()
      }
}