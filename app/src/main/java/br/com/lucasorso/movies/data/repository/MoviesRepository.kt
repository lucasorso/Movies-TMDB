package br.com.lucasorso.movies.data.repository

import br.com.lucasorso.movies.data.database.entities.Genre
import br.com.lucasorso.movies.data.database.entities.Movie
import br.com.lucasorso.movies.data.network.detail.DetailsRequest
import br.com.lucasorso.movies.data.network.search.SearchRequest
import br.com.lucasorso.movies.data.network.upcoming.UpcomingRequest
import io.reactivex.Single

interface MoviesRepository {

    @Throws(Exception::class)
    fun searchMovies(request: SearchRequest): Single<List<Movie>>

    @Throws(Exception::class)
    fun fetchUpcomingMovies(request: UpcomingRequest): Single<List<Movie>>

    @Throws(Exception::class)
    fun fetchMovieDetails(request: DetailsRequest): Single<List<Genre>>

    fun getMovieDetails(request: DetailsRequest): Single<Movie>

}