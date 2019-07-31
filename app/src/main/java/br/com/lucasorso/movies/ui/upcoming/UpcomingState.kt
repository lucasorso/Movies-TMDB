package br.com.lucasorso.movies.ui.upcoming

import java.io.Serializable

class UpcomingState(
        var page: Int = 1,
        var isLoading: Boolean = false,
        var isLastPage: Boolean = false) : Serializable