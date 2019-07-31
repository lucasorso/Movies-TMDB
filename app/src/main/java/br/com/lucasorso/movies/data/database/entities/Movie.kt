package br.com.lucasorso.movies.data.database.entities

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Movie(
        @PrimaryKey
        var idMovie: Long? = null,
        var posterPath: String? = null,
        var isAdult: Boolean? = false,
        var overview: String? = null,
        var releaseDate: String? = null,
        var genreIds: RealmList<Int>? = RealmList(),
        var originalTitle: String? = null,
        var originalLanguage: String? = null,
        var title: String? = null,
        var backdropPath: String? = null,
        var popularity: Double? = null,
        var voteCount: Int? = null,
        var video: Boolean = false,
        var voteAverage: Double? = null
) : RealmObject()