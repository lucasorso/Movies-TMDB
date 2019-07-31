package br.com.lucasorso.movies.data.database

import io.realm.DynamicRealm
import io.realm.RealmMigration


class MigrationProvider : RealmMigration {

    companion object{
        const val CURRENT_REALM_VERSION = 0L
    }

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
    }

}