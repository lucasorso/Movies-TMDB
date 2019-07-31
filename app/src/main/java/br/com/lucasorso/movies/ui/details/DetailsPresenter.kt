package br.com.lucasorso.movies.ui.details

import br.com.lucasorso.movies.data.network.detail.DetailsRequest
import br.com.lucasorso.movies.data.repository.MoviesProvider
import br.com.lucasorso.movies.domain.interactor.DetailsInteractor
import br.com.lucasorso.movies.domain.model.MovieModel
import br.com.lucasorso.movies.ui.BasePresenter
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class DetailsPresenter : BasePresenter<DetailsContract>() {

    private val interactor = DetailsInteractor(
            MoviesProvider(),
            Schedulers.io())

    fun init(idMovie: Int) {
        mViewContract?.showLoading()
        interactor.execute(DetailsObserver(), DetailsRequest(idMovie))
    }

    override fun destroy() {
        interactor.dispose()
        super.destroy()
    }

    inner class DetailsObserver : DisposableObserver<MovieModel>() {

        override fun onComplete() {
            mViewContract?.hideLoading()
            println("onCompleted")
        }

        override fun onNext(t: MovieModel) {
            mViewContract?.onDetailsReceive(t)
        }

        override fun onError(e: Throwable) {
            mViewContract?.hideLoading()
            e.message?.let { mViewContract?.showErrorMessage(it) }
            println("OnError: ${e.message}")
        }

    }

}