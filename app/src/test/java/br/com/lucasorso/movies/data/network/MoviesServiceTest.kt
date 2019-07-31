package br.com.lucasorso.movies.data.network

import android.content.Context
import br.com.lucasorso.movies.data.network.detail.DetailsRequest
import br.com.lucasorso.movies.data.network.search.SearchRequest
import br.com.lucasorso.movies.data.network.upcoming.UpcomingRequest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MoviesServiceTest {

    lateinit var context: Context

    @Before
    fun setup() {
        context = Mockito.mock(Context::class.java)
    }
    @Test
    fun searchMovies() {
        val moviesService = MoviesService()
        val searchRequest = SearchRequest("Clube", 1)
        val foundMoviesList = moviesService.searchMovies(searchRequest)
        assert(foundMoviesList!!.isNotEmpty())
    }

    @Test
    fun getUpcomingRelesases() {
        val moviesService = MoviesService()
        val upcomingRequest = UpcomingRequest(1)
        val moviesList = moviesService.getUpcomingMovies(upcomingRequest)
        assert(moviesList!!.isNotEmpty())
    }

    @Test
    fun getMovieDetails() {
        val moviesService = MoviesService()
        val detailsResquest = DetailsRequest(420818)
        val movieDetails = moviesService.getMovieDetails(detailsResquest)
        val genreList = movieDetails?.genreList
        assert(genreList!!.isNotEmpty())
    }
}