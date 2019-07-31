package br.com.lucasorso.movies.domain.interactor

import br.com.lucasorso.movies.data.network.search.SearchRequest
import br.com.lucasorso.movies.data.repository.MoviesProvider
import br.com.lucasorso.movies.domain.Interactor
import br.com.lucasorso.movies.domain.model.MovieModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

class SearchInteractor(private val provider: MoviesProvider,
                       private val threadExecutor: Scheduler,
                       private val threadObserver: Scheduler = AndroidSchedulers.mainThread())
    : Interactor<List<MovieModel>, SearchRequest>(threadExecutor, threadObserver) {

    override fun buildUseCaseObservable(request: SearchRequest):
            Observable<List<MovieModel>> {

        return provider.searchMovies(request)
                .flatMap { list ->
                    Observable.fromIterable(list)
                            .map { movie ->
                                MovieModel(
                                        movie.idMovie?.toInt(),
                                        movie.title,
                                        movie.releaseDate,
                                        movie.posterPath,
                                        movie.backdropPath,
                                        movie.overview)
                            }.toList()
                }
                .toObservable()
    }

}