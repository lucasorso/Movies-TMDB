package br.com.lucasorso.movies.data.network.detail

import br.com.lucasorso.movies.data.network.BaseRequest
import com.google.gson.annotations.SerializedName

class DetailsRequest(@SerializedName("movie_id") var idMovie: Int) : BaseRequest()