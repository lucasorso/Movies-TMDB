package br.com.lucasorso.movies.domain.interactor

import android.content.Context
import br.com.lucasorso.movies.data.network.search.SearchRequest
import br.com.lucasorso.movies.data.repository.MoviesProvider
import br.com.lucasorso.movies.domain.model.MovieModel
import br.com.lucasorso.movies.ui.connectionTypeAvailabe
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.observers.DisposableObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class SearchInteractorRealmTest {

    lateinit var context: Context

    @Before
    fun setup() {
        context = Mockito.mock(Context::class.java)

        val immediate = object : Scheduler() {
            override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor {
                    it.run()
                }, false)
            }
        }
        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
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
