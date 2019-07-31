package br.com.lucasorso.movies.data.network

import com.google.gson.annotations.SerializedName

abstract class ErrorResponse {

    @SerializedName("status_code")
    var code: Int? = null

    @SerializedName("status_message")
    var message: String? = null

    @SerializedName("success")
    var success: Boolean? = null
}