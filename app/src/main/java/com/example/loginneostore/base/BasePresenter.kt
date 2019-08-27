package com.example.loginneostore.base

import androidx.annotation.Nullable
import com.example.loginneostore.common.PopView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException

abstract class BasePresenter {

    private val disposables = CompositeDisposable()
    private val retryCount: Short = 2
    abstract fun loadPresenter()


    internal fun destroyPresenter() {

        disposables.clear()
        disposables.dispose()

    }

    protected fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    internal fun onError(throwable: Throwable, @Nullable loaderView: PopView?) {

        loaderView?.let {
            it.dismissPopLoading()
            it.popError(throwable.message.toString())
        }
    }

    protected fun onError(throwable: Throwable) {
        onError(throwable, null)
    }


    fun onResumePresenter() {}

    protected fun <THREAD_OWNER> loadNetworkThread(upstream: Observable<THREAD_OWNER>)
            : Observable<THREAD_OWNER> {
        return upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry { integer, throwable -> integer < retryCount && throwable is SocketTimeoutException }
    }

    protected fun <THREAD_OWNER> loadSingleNetworkThread(upstream: Single<THREAD_OWNER>): SingleSource<THREAD_OWNER> {
        return upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry { integer, throwable -> integer < retryCount && throwable is SocketTimeoutException }
    }


}