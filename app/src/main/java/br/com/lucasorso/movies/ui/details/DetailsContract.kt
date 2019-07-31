package br.com.lucasorso.movies.ui.details

import br.com.lucasorso.movies.domain.model.MovieModel
import br.com.lucasorso.movies.ui.IContract

interface DetailsContract : IContract {

    fun onDetailsReceive(movie: MovieModel)
}