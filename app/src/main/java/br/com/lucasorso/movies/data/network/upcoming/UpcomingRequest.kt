package br.com.lucasorso.movies.data.network.upcoming

import br.com.lucasorso.movies.data.network.BaseRequest
import com.google.gson.annotations.SerializedName

class UpcomingRequest(@SerializedName("page") var page: Int) : BaseRequest()