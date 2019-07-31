package br.com.lucasorso.movies.data.database.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Company(
        @PrimaryKey
        var idCompany: Long? = null,
        var logoPath: String? = null,
        var name: String? = null,
        var originCountry: String? = null
) : RealmObject()