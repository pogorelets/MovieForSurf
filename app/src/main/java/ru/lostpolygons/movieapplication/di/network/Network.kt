package ru.lostpolygons.movieapplication.di.network

import android.app.Application
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import retrofit2.HttpException
import ru.lostpolygons.movieapplication.R

class Network(application: Application, private val gson: Gson) {
    private val unknownNetworkException =
        UnknownNetworkException(application.getString(R.string.network_request_error))

    fun <T> prepareRequest(request: Single<T>): Single<T> = request.compose(errorValidation<T>())

    private fun <T> errorValidation(): SingleTransformer<T, T> = SingleTransformer { single ->
        single.onErrorResumeNext { Single.error(wrapError(it)) }
    }

    fun prepareRequest(request: Completable): Completable = request.compose(errorValidation())

    private fun errorValidation(): CompletableTransformer = CompletableTransformer { completable ->
        completable.onErrorResumeNext { Completable.error(wrapError(it)) }
    }

    private fun wrapError(throwable: Throwable): RuntimeException {
        if (throwable is HttpException) {
            val reader = throwable.response().errorBody()?.charStream()
                ?: return unknownNetworkException
            //TODO здесь можно при необходимости  распарсить ошибку
            val message = reader.toString()
            return NetworkException(message)
        } else {
            return unknownNetworkException
        }
    }
}