package br.com.lucasorso.movies

import android.app.Application
import br.com.lucasorso.movies.data.database.MigrationProvider
import br.com.lucasorso.movies.data.database.MigrationProvider.Companion.CURRENT_REALM_VERSION
import io.realm.Realm
import io.realm.RealmConfiguration

class MoviesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initRealm()
        INSTANCE = this
    }

    private fun initRealm() {
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
                .name("movies-realm")
                .schemaVersion(CURRENT_REALM_VERSION)
                .migration(MigrationProvider())
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    companion object {
        private lateinit var INSTANCE: MoviesApplication
        fun getInstance(): MoviesApplication {
            return INSTANCE
        }
    }
}

