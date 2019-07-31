package br.com.lucasorso.movies.domain.interactor

import br.com.lucasorso.movies.data.database.entities.Genre
import br.com.lucasorso.movies.data.database.entities.Movie
import br.com.lucasorso.movies.data.network.detail.DetailsRequest
import br.com.lucasorso.movies.data.repository.MoviesProvider
import br.com.lucasorso.movies.domain.Interactor
import br.com.lucasorso.movies.domain.model.MovieModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction


class DetailsInteractor(
        private val provider: MoviesProvider,
        private val threadExecutor: Scheduler,
        private val threadObserver: Scheduler = AndroidSchedulers.mainThread())
    : Interactor<MovieModel, DetailsRequest>(threadExecutor, threadObserver) {

    override fun buildUseCaseObservable(request: DetailsRequest):
            Observable<MovieModel> {

        val genreList = provider.fetchMovieDetails(request)
        val movie = provider.getMovieDetails(request)

        return movie.zipWith(genreList,
                BiFunction<Movie, List<Genre>, MovieModel> { movieEntity, listOfGenre ->
                    MovieModel(movieEntity.idMovie?.toInt(),
                            movieEntity.title,
                            movieEntity.releaseDate,
                            movieEntity.posterPath,
                            movieEntity.backdropPath,
                            movieEntity.overview,
                            listOfGenre.map { it.name })
                })
                .toObservable()
    }

}