package br.com.lucasorso.movies.data.network

import com.google.gson.annotations.SerializedName

abstract class BaseRequest {

    @SerializedName("api_key")
    val apiKey = RestClient.API_KEY

    @SerializedName("language")
    var language: String = RestClient.LANGUAGE

    @SerializedName("region")
    var region: String = RestClient.REGION
}


