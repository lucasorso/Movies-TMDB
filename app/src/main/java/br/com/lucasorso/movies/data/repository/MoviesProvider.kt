package br.com.lucasorso.movies.data.repository

import br.com.lucasorso.movies.MoviesApplication
import br.com.lucasorso.movies.data.database.entities.Company
import br.com.lucasorso.movies.data.database.entities.Genre
import br.com.lucasorso.movies.data.database.entities.Movie
import br.com.lucasorso.movies.data.exception.NoInternetConnection
import br.com.lucasorso.movies.data.exception.NoMoviesFound
import br.com.lucasorso.movies.data.network.MoviesService
import br.com.lucasorso.movies.data.network.detail.DetailsRequest
import br.com.lucasorso.movies.data.network.search.SearchRequest
import br.com.lucasorso.movies.data.network.upcoming.MovieResponse
import br.com.lucasorso.movies.data.network.upcoming.UpcomingRequest
import br.com.lucasorso.movies.ui.connectionTypeAvailabe
import io.reactivex.Single
import io.realm.Case
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.oneOf

class MoviesProvider : MoviesRepository {

    private val hasInternet: Boolean
        get() = MoviesApplication.getInstance().connectionTypeAvailabe() != 0

    private val movieService = MoviesService()

    override fun searchMovies(request: SearchRequest): Single<List<Movie>> {
        return Single.create { observer ->
            try {
                if (hasInternet) {
                    val realm = Realm.getDefaultInstance()
                    val searchMovies = movieService.searchMovies(request)
                    val entities = searchMovies.mapToEntity()
                    if (entities != null) {
                        realm.executeTransaction { realmInstance ->
                            realmInstance.insertOrUpdate(entities)
                        }
                        observer.onSuccess(entities)
                    } else {
                        observer.onError(NoMoviesFound())
                    }
                } else {
                    val realm = Realm.getDefaultInstance()
                    val entities = realm.copyFromRealm(
                            realm.where(Movie::class.java)
                                    .contains("title", request.query, Case.INSENSITIVE)
                                    .sort("title")
                                    .findAll())
                    if (entities.isNotEmpty()) {
                        observer.onSuccess(entities)
                    } else {
                        observer.onError(NoMoviesFound())
                    }
                }
            } catch (a: Exception) {
                a.printStackTrace()
                observer.onError(a)
            }
        }
    }

    override fun fetchUpcomingMovies(request: UpcomingRequest): Single<List<Movie>> {
        return Single.create { observer ->
            try {
                if (hasInternet) {
                    val upcomingRelesases = movieService.getUpcomingMovies(request)
                    val entities = upcomingRelesases.mapToEntity()

                    if (entities != null) {
                        Realm.getDefaultInstance()
                                .executeTransaction { realmInstance ->
                                    realmInstance.insertOrUpdate(entities)
                                }
                        observer.onSuccess(entities)
                    }
                } else {
                    val entities = mutableListOf<Movie>()
                    Realm.getDefaultInstance()
                            .executeTransaction { realm ->
                                val realmResults = realm.where(Movie::class.java)
                                        .findAll()
                                if (realmResults.isNotEmpty()) {
                                    entities.addAll(realm.copyFromRealm(realmResults))
                                }
                            }
                    if (entities.isNotEmpty()) {
                        observer.onSuccess(entities.toList())
                    } else {
                        observer.onError(NoInternetConnection())
                    }
                }
            } catch (a: Exception) {
                a.printStackTrace()
                observer.onError(a)
            }
        }
    }

    override fun fetchMovieDetails(request: DetailsRequest): Single<List<Genre>> {
        return Single.create { observer ->
            try {
                if (hasInternet) {
                    val detailsResponse = movieService.getMovieDetails(request)
                    val genreList = detailsResponse
                            ?.genreList
                            ?.map { response ->
                                Genre(response.id, response.name)
                            }
                    val companiesList = detailsResponse
                            ?.prodCompaniesList
                            ?.map { response ->
                                Company(response.id.toLong(),
                                        response.logoPath,
                                        response.name,
                                        response.originCountry)
                            }

                    val realm = Realm.getDefaultInstance()
                    realm.beginTransaction()
                    genreList?.let { realm.insertOrUpdate(it) }
                    companiesList?.let { realm.insertOrUpdate(it) }
                    realm.commitTransaction()
                    realm.close()
                    if (genreList != null) {
                        observer.onSuccess(genreList)
                    }
                } else {
                    val genreList = mutableListOf<Genre>()
                    val realm = Realm.getDefaultInstance()
                    realm.beginTransaction()
                    val movie = realm.where(Movie::class.java)
                            .equalTo("idMovie", request.idMovie)
                            .findFirst()

                    if (movie?.genreIds!!.isNotEmpty()) {
                        val realmResults = realm.where(Genre::class.java)
                                .oneOf("idGenre", movie.genreIds!!.toTypedArray())
                                .findAll()
                        genreList.addAll(realm.copyFromRealm(realmResults))
                    }
                    realm.commitTransaction()
                    realm.close()
                    observer.onSuccess(genreList.toList())
                }
            } catch (a: Exception) {
                a.printStackTrace()
                observer.onError(a)
            }
        }
    }

    override fun getMovieDetails(request: DetailsRequest): Single<Movie> {
        return Single.create { observer ->
            try {
                val realm = Realm.getDefaultInstance()
                val movie = realm.copyFromRealm(
                        realm.where(Movie::class.java)
                                .equalTo("idMovie", request.idMovie)
                                .findFirst())
                observer.onSuccess(movie!!)
            } catch (a: Exception) {
                a.printStackTrace()
                observer.onError(a)
            }
        }
    }
}

private fun List<MovieResponse>?.mapToEntity(): List<Movie>? {
    return this?.map { response ->
        Movie(response.id,
                response.posterPath,
                response.isAdult,
                response.overview,
                response.releaseDate,
                response.genreIds.convertToRealmList(),
                response.originalTitle,
                response.originalLanguage,
                response.title,
                response.backdropPath,
                response.popularity,
                response.voteCount,
                response.isAdult,
                response.voteAverage)
    }
}

fun <E> List<E>?.convertToRealmList(): RealmList<E>? {
    val realmList = RealmList<E>()
    this?.forEach {
        realmList.add(it)
    }
    return realmList
}
