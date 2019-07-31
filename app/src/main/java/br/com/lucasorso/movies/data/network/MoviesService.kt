package br.com.lucasorso.movies.data.network

import br.com.lucasorso.movies.data.network.detail.DetailsRequest
import br.com.lucasorso.movies.data.network.detail.DetailsResponse
import br.com.lucasorso.movies.data.network.search.SearchRequest
import br.com.lucasorso.movies.data.network.upcoming.MovieResponse
import br.com.lucasorso.movies.data.network.upcoming.UpcomingRequest
import br.com.lucasorso.movies.data.network.upcoming.UpcomingResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException
import java.net.HttpURLConnection.HTTP_OK

class MoviesService {

    interface API {

        @GET("/3/search/movie")
        fun searchMovies(@Query("api_key") apiKey: String,
                         @Query("query") query: String,
                         @Query("language") language: String,
                         @Query("region") region: String,
                         @Query("page") page: Int,
                         @Query("include_adult") adult: Boolean):
                Call<UpcomingResponse>

        @GET("/3/movie/upcoming?")
        fun getUpcomingMovies(@Query("api_key") apiKey: String,
                              @Query("language") language: String,
                              @Query("region") region: String,
                              @Query("page") page: String):
                Call<UpcomingResponse>

        @GET("/3/movie/{id}")
        fun getMovieDetails(@Path("id") id: Int,
                            @Query("api_key") apiKey: String,
                            @Query("language") language: String):
                Call<DetailsResponse>
    }

    @Throws(IOException::class, NullPointerException::class, Exception::class)
    fun searchMovies(request: SearchRequest): List<MovieResponse>? {
        val retrofit = RestClient().getRetrofit()

        val api = retrofit.create(API::class.java)

        val call = api.searchMovies(
                request.apiKey,
                request.query,
                request.language,
                request.region,
                request.page,
                request.adult)

        val response = call.execute()

        if (response.code() == HTTP_OK) {
            if (response.body() is UpcomingResponse) {
                return response.body()?.mMovieResponseList
            } else {
                val errorResponse = Gson()
                        .fromJson(response.errorBody().toString(),
                                ErrorResponse::class.java)
                throw Exception(errorResponse.message)
            }
        }
        return null
    }


    @Throws(IOException::class, NullPointerException::class, Exception::class)
    fun getUpcomingMovies(request: UpcomingRequest): List<MovieResponse>? {
        val retrofit = RestClient().getRetrofit()

        val api = retrofit.create(API::class.java)

        val call = api.getUpcomingMovies(
                request.apiKey,
                request.language,
                request.region,
                request.page.toString())

        val response = call.execute()

        if (response.code() == HTTP_OK) {
            if (response.body() is UpcomingResponse) {
                return response.body()?.mMovieResponseList
            } else {
                val errorResponse = Gson()
                        .fromJson(response.errorBody().toString(),
                                ErrorResponse::class.java)
                throw Exception(errorResponse.message)
            }
        }
        return null
    }


    @Throws(IOException::class, NullPointerException::class, Exception::class)
    fun getMovieDetails(request: DetailsRequest): DetailsResponse? {
        val retrofit = RestClient().getRetrofit()

        val api = retrofit.create(API::class.java)

        val call = api.getMovieDetails(
                request.idMovie,
                request.apiKey,
                request.language)

        val response = call.execute()

        if (response.code() == HTTP_OK) {
            if (response.body() is DetailsResponse) {
                return response.body()
            } else {
                val errorResponse = Gson()
                        .fromJson(response.errorBody().toString(),
                                ErrorResponse::class.java)
                throw Exception(errorResponse.message)
            }
        }
        return null
    }


}