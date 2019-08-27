package com.example.loginneostore.util

import com.example.loginneostore.base.DataMapper
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

fun <T> Single<T>.onApiExecute(
    onStart: () -> Unit,
    attachThread: (Single<T>) -> SingleSource<T>,
    onStop: () -> Unit
): Single<T> {

    return this
        .compose {
            attachThread(it)
        }
        .doOnSubscribe { onStart() }
        .doFinally { onStop() }

}


fun <T, F> Single<T>.onApiDataMapper(dataMapper: DataMapper<T, F>): Single<F> {

    return this.map {
        dataMapper.mapper(it)
    }
}


fun <T> Single<T>.apiSuccess(successMap: (T) -> Unit): Single<T> {
    return this.doAfterSuccess {
        successMap(it)
    }
}

fun <T> Single<T>.apiFail(failMap: (Throwable) -> Unit): Single<T> {
    return this.doOnError {
        failMap(it)
    }
}


fun <T, F> Single<T>.onApiResponseMapper(
    waiter: Long = 0,
    isSuccess: (T) -> Boolean,
    mapSuccess: (T) -> F,
    mapFail: (T) -> Throwable
): Single<F> {
    return this.flatMap {

            res ->
        return@flatMap Single.timer(waiter, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                Single.create<F> { emitter ->

                    if (isSuccess(res)) {
                        emitter.onSuccess(mapSuccess(res))
                    } else {
                        emitter.onError(mapFail(res))
                    }
                }
            }


    }
}


fun <T> Single<T>.call(): Disposable {
    return this.subscribe({}, {})

}






