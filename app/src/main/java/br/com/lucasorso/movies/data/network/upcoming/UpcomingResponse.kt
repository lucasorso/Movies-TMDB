package br.com.lucasorso.movies.data.network.upcoming

import com.google.gson.annotations.SerializedName

class UpcomingResponse {

    @SerializedName("total_pages")
    var totalPages: Int = 0

    @SerializedName("total_results")
    var totalResults: Int = 0

    @SerializedName("results")
    internal var mMovieResponseList: List<MovieResponse>? = emptyList()

}

class MovieResponse {

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("vote_count")
    var voteCount: Int = 0

    @SerializedName("video")
    var video: Boolean = false

    @SerializedName("vote_average")
    var voteAverage: Double = 0.toDouble()

    @SerializedName("title")
    var title: String? = null

    @SerializedName("popularity")
    var popularity: Double = 0.toDouble()

    @SerializedName("poster_path")
    var posterPath: String? = null

    @SerializedName("original_language")
    var originalLanguage: String? = null

    @SerializedName("original_title")
    var originalTitle: String? = null

    @SerializedName("genre_ids")
    var genreIds: List<Int>? = emptyList()

    @SerializedName("backdrop_path")
    var backdropPath: String? = null

    @SerializedName("adult")
    var isAdult: Boolean = false

    @SerializedName("overview")
    var overview: String? = null

    @SerializedName("release_date")
    var releaseDate: String? = null
}