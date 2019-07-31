package br.com.lucasorso.movies.data.exception

import br.com.lucasorso.movies.R

class NoInternetConnection(
        val errorMessage: Int = R.string.no_internet_connection,
        val helperMessage: Int = R.string.internet_helper_messa
) : Exception()

class NoMoviesFound(
        val errorMessage: Int = R.string.no_movies_found,
        val helperMessage: Int = R.string.internet_helper_messa
) : Exception()