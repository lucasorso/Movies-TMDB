package br.com.lucasorso.movies.domain.model

import br.com.lucasorso.movies.data.network.RestClient

class MovieModel(var id: Int?,
                 var title: String?,
                 var releaseDate: String?,
                 posterPath: String?,
                 backdropPath: String?,
                 var overView: String?,
                 var genresList: List<String?> = emptyList()) {

    var posterPath = posterPath
        get() = RestClient.IMAGE_URL + "w185" + field
    var backdropPath = backdropPath
        get() = RestClient.IMAGE_URL + "w500" + field
}