package br.com.lucasorso.movies.data.network.detail

import com.google.gson.annotations.SerializedName

class DetailsResponse {

    @SerializedName("genres")
    internal var genreList: List<Genre>? = emptyList()

    @SerializedName("production_companies")
    internal var prodCompaniesList: List<ProductionCompanies>? = emptyList()
}

class Genre {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null
}

class ProductionCompanies {


    @SerializedName("id")
    var id: Int = 0

    @SerializedName("logo_path")
    var logoPath: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("origin_country")
    var originCountry: String? = null

}