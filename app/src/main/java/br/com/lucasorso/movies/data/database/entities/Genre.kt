package br.com.lucasorso.movies.data.database.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Genre(
        @PrimaryKey var idGenre: Int? = null,
        var name: String? = null
) : RealmObject()