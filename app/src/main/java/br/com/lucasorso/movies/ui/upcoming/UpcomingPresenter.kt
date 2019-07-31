package br.com.lucasorso.movies.ui.upcoming

import br.com.lucasorso.movies.R
import br.com.lucasorso.movies.data.exception.NoInternetConnection
import br.com.lucasorso.movies.data.exception.NoMoviesFound
import br.com.lucasorso.movies.data.network.search.SearchRequest
import br.com.lucasorso.movies.data.network.upcoming.UpcomingRequest
import br.com.lucasorso.movies.data.repository.MoviesProvider
import br.com.lucasorso.movies.domain.interactor.SearchInteractor
import br.com.lucasorso.movies.domain.interactor.UpcomingInteractor
import br.com.lucasorso.movies.domain.model.MovieModel
import br.com.lucasorso.movies.ui.BasePresenter
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.io.Serializable

class UpcomingPresenter : BasePresenter<UpcomingContract>() {

    private val upcomingState by lazy { UpcomingState() }
    private var searchState: SearchState? = null
    private val FIRST_PAGE = 1
    var isAllReadyFetched = false

    private val upcomingInteractor: UpcomingInteractor = UpcomingInteractor(
            MoviesProvider(),
            Schedulers.io())

    private val searchInteractor: SearchInteractor = SearchInteractor(
            MoviesProvider(),
            Schedulers.io())

    override fun resume() {
        super.resume()
        println("Upcoming Presenter resume called")
    }

    fun fetchUpcoming() {
        if (isAllReadyFetched.not()) {
            isAllReadyFetched = true
            mViewContract?.showLoading()
            execute()
        }
    }

    private fun fetchMoreUpcomingMovies() {
        upcomingState.isLoading = true
        upcomingState.page = upcomingState.page.inc()
        mViewContract?.showLoading()
        execute()
    }

    private fun execute() {
        upcomingInteractor.execute(UpcomingObserver(), UpcomingRequest(upcomingState.page))
    }

    private fun onReceiveFromSearch(list: List<MovieModel>) {
        if (searchState?.page != FIRST_PAGE) {
            if (list.isEmpty()) {
                searchState?.isLastPage = true
                searchState?.page = searchState?.page!!.dec()
            } else {
                searchState?.isLoading = false
                mViewContract?.renderMoreFoundMovies(list)
            }
        } else {
            searchState?.isLoading = false
            if (list.isEmpty()) {
                mViewContract?.showMessage(R.string.no_movies_found, true)
            } else {
                mViewContract?.renderFoundMovieList(list)
            }
        }
    }

    private fun onReceiveFromUpcoming(list: List<MovieModel>) {
        if (upcomingState.page != FIRST_PAGE) {
            if (list.isEmpty()) {
                upcomingState.isLastPage = true
                upcomingState.page = upcomingState.page.dec()
            } else {
                upcomingState.isLoading = false
                mViewContract?.renderMoreMovies(list)
            }
        } else {
            upcomingState.isLoading = false
            mViewContract?.renderMovieList(list)
        }
    }

    override fun destroy() {
        upcomingInteractor.dispose()
        searchInteractor.dispose()
        super.destroy()
    }

    fun searchMovies(querySearch: String) {
        searchState!!.query = querySearch
        searchState!!.page = 1
        search()
    }

    private fun searhMoreMovies() {
        searchState?.page = searchState?.page!!.inc()
        search()
    }

    fun setSearchState(searchState: SearchState){
        this.searchState = searchState
    }

    fun getSearchState(): Serializable? {
        return searchState
    }

    private fun search() {
        searchState?.isLoading = true
        mViewContract?.showLoading()
        searchInteractor.execute(SearchObserver(),
                SearchRequest(searchState?.query!!, searchState?.page!!))
    }

    fun enableSearch() {
        searchState = SearchState()
    }

    fun disableSearch() {
        searchState = null
    }

    fun isSearchEnable() = searchState != null

    fun getQuery() = searchState?.query

    fun isLoading(): Boolean {
        return if (isSearchEnable()) {
            searchState?.isLoading!!
        } else {
            upcomingState.isLoading
        }
    }

    fun isLastPage(): Boolean {
        return if (isSearchEnable()) {
            searchState?.isLastPage!!
        } else {
            upcomingState.isLastPage
        }
    }

    fun loadMoreItens() {
        if (isSearchEnable()) {
            searhMoreMovies()
        } else {
            fetchMoreUpcomingMovies()
        }
    }

    /**
     * Inner Classes
     */
    inner class UpcomingObserver : DisposableObserver<List<MovieModel>>() {
        override fun onComplete() {
            mViewContract?.hideLoading()
        }

        override fun onNext(list: List<MovieModel>) {
            onReceiveFromUpcoming(list)
        }

        override fun onError(e: Throwable) {
            mViewContract?.hideLoading()
            when (e) {
                is NoInternetConnection -> {
                    mViewContract?.showErrorMessage(e.errorMessage, false)
                    mViewContract?.showSnacbarkHint(e.helperMessage)
                }
                is NoMoviesFound -> {
                    mViewContract?.showErrorMessage(e.errorMessage, false)
                }
                else -> mViewContract?.showErrorMessage(e.message!!, true)
            }
        }
    }

    inner class SearchObserver : DisposableObserver<List<MovieModel>>() {
        override fun onComplete() {
            mViewContract?.hideLoading()
        }

        override fun onNext(list: List<MovieModel>) {
            onReceiveFromSearch(list)
        }

        override fun onError(e: Throwable) {
            mViewContract?.hideLoading()
            when (e) {
                is NoInternetConnection -> {
                    mViewContract?.showErrorMessage(e.errorMessage, false)
                    mViewContract?.showSnacbarkHint(e.helperMessage)

                }
                is NoMoviesFound -> {
                    mViewContract?.showErrorMessage(e.errorMessage, false)
                }
                else -> mViewContract?.showErrorMessage(e.message!!, true)
            }
        }
    }
}

