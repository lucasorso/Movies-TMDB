package br.com.lucasorso.movies.ui.upcoming

import java.io.Serializable

class SearchState(
        var query: String = "",
        var page: Int = 1,
        var isLoading: Boolean = false,
        var isLastPage: Boolean = false) : Serializable


