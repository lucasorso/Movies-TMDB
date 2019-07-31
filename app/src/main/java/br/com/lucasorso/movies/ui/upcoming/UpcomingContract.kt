package br.com.lucasorso.movies.ui.upcoming

import br.com.lucasorso.movies.domain.model.MovieModel
import br.com.lucasorso.movies.ui.IContract

interface UpcomingContract : IContract {

    fun renderMovieList(list: List<MovieModel>)

    fun renderMoreMovies(list: List<MovieModel>)

    fun onMoviewClick(movie: MovieModel)

    fun renderFoundMovieList(list: List<MovieModel>)

    fun renderMoreFoundMovies(list: List<MovieModel>)

    fun showSnacbarkHint(message: Int)

}