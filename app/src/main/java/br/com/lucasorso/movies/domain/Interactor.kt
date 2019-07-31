package br.com.lucasorso.movies.domain

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

abstract class Interactor<Data, Request>(private val threadExecutor: Scheduler,
                                             private val threadObserver: Scheduler) {

    private val dispoables: CompositeDisposable = CompositeDisposable()

    internal abstract fun buildUseCaseObservable(request: Request): Observable<Data>

    fun execute(data: DisposableObserver<Data>, request: Request) {
        val observable = buildUseCaseObservable(request)
                .subscribeOn(threadExecutor)
                .observeOn(threadObserver)
        addDisposable(observable.subscribeWith(data))
    }

    fun dispose() {
        if (dispoables.isDisposed.not()) {
            dispoables.dispose()
        }
    }

    private fun addDisposable(disposable: Disposable) {
        dispoables.add(disposable)
    }
}
