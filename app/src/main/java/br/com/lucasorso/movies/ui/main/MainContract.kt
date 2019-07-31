package br.com.lucasorso.movies.ui.main

import br.com.lucasorso.movies.ui.IContract

interface MainContract : IContract {

    fun navigateToDetails(movieId: Int?)
}