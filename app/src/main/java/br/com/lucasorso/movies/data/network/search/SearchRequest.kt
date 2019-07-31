package br.com.lucasorso.movies.data.network.search

import br.com.lucasorso.movies.data.network.BaseRequest
import com.google.gson.annotations.SerializedName

class SearchRequest(@SerializedName("query") var query: String,
                    @SerializedName("page") var page: Int,
                    @SerializedName("include_adult") var adult: Boolean = false)
    : BaseRequest()