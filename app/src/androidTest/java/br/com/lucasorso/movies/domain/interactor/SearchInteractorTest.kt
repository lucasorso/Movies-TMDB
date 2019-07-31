package br.com.lucasorso.movies.domain.interactor

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import br.com.lucasorso.movies.data.network.search.SearchRequest
import br.com.lucasorso.movies.data.repository.MoviesProvider
import br.com.lucasorso.movies.domain.model.MovieModel
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchInteractorTest {

    lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context

        Realm.init(context)
        val testConfig = RealmConfiguration
                .Builder()
                .inMemory()
                .name("test-realm")
                .build()

        Realm.setDefaultConfiguration(testConfig)
    }

    @Test
    fun testSearhInteractor() {
        val searchRequest = SearchRequest("Top gun", 1, false)
        val searchInteractor = SearchInteractor(
                MoviesProvider(), Schedulers.io())

        searchInteractor.execute(object : DisposableObserver<List<MovieModel>>() {
            override fun onComplete() {/*Nothing*/
            }

            override fun onNext(t: List<MovieModel>) {
                assert(t.isNotEmpty())
            }

            override fun onError(e: Throwable) {/*Nothing*/
            }
        }, searchRequest)
    }
}
