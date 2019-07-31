package br.com.lucasorso.movies.ui.upcoming

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.lucasorso.movies.R
import br.com.lucasorso.movies.domain.model.MovieModel
import br.com.lucasorso.movies.ui.BaseFragment
import br.com.lucasorso.movies.ui.CustomScrollListener
import br.com.lucasorso.movies.ui.HasPresenter
import br.com.lucasorso.movies.ui.main.MainContract
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.GridLayoutManager

class UpcomingFragment :
        BaseFragment(),
        UpcomingContract,
        HasPresenter<UpcomingPresenter> {

    private val adapter by lazy { UpcomingRcAdapter(mutableListOf(), this::onMoviewClick) }
    private val adapterSearch by lazy { UpcomingRcAdapter(mutableListOf(), this::onMoviewClick) }
    private var listener: MainContract? = null
    private lateinit var viewRoot: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var searchMenuItem: MenuItem

    override val presenter = UpcomingPresenter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainContract) {
            listener = context
        } else {
            throw RuntimeException("$context must implement MainContract")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE){
            (recyclerView.layoutManager as GridLayoutManager).spanCount = 4

        } else if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            (recyclerView.layoutManager as GridLayoutManager).spanCount = 2
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.setContract(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        println("onCreateView")
        return inflater.inflate(R.layout.fragment_upcomging, container, false)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val searchEnable = savedInstanceState?.getBoolean("isSearching")
        searchEnable?.let {enable ->
            if (enable) {
                val serializable = savedInstanceState.getSerializable("searchState")
                serializable?.let { state ->
                    presenter.setSearchState(state as SearchState)
                    Handler().postDelayed({
                        recyclerView.adapter = adapterSearch
                        MenuItemCompat.expandActionView(searchMenuItem)
                        searchView.setQuery(presenter.getQuery(), false)
                        searchView.clearFocus()
                        presenter.searchMovies(presenter.getQuery().toString())
                    },400)
                }
            }
        }
        println("onViewStateRestored")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchEnable = presenter.isSearchEnable()
        if (searchEnable) {
            outState.putBoolean("isSearching", searchEnable)
            outState.putSerializable("searchState", presenter.getSearchState())
        }
        println("onSaveInstanceState")
    }

    override fun showSnacbarkHint(message: Int) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG).show() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponets(view)
        initRc()
        initPresenter()
    }

    private fun initPresenter() {
        presenter.fetchUpcoming()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search, menu)
        searchMenuItem = menu?.findItem(R.id.action_search)!!
        searchView = searchMenuItem.actionView as SearchView
        initSerchView()
    }

    private fun initComponets(view: View) {
        viewRoot = view.findViewById(R.id.root)
        viewRoot.apply {
            setColorSchemeResources(
                    R.color.googleGreen,
                    R.color.googleBlue,
                    R.color.googleRed,
                    R.color.googleYellow)
            isEnabled = false

            if (presenter.isSearchEnable()) {
                doOnLayout {
                    MenuItemCompat.expandActionView(searchMenuItem)
                    searchView.setQuery(presenter.getQuery(), false)
                    searchView.clearFocus()
                }
            }
        }
        recyclerView = view.findViewById(R.id.upcomingList)
    }

    private fun initRc() {
        recyclerView.addOnScrollListener(object :
                CustomScrollListener(recyclerView.layoutManager!!) {
            override fun loadMoreItens() {
                presenter.loadMoreItens()

            }

            override fun isLastPage(): Boolean {
                return presenter.isLastPage()
            }

            override fun isLoading(): Boolean {
                return presenter.isLoading()
            }
        })
        recyclerView.adapter = if (presenter.isSearchEnable()) adapterSearch else adapter
    }

    private fun initSerchView() {
        searchView.apply {
            maxWidth = Int.MAX_VALUE
        }
        searchView.queryHint = getString(R.string.query_hint)
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, object :
                MenuItemCompat.OnActionExpandListener {

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                if (presenter.isSearchEnable().not()) {
                    presenter.enableSearch()
                    recyclerView.adapter = adapterSearch
                }
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                presenter.disableSearch()
                recyclerView.adapter = adapter
                adapterSearch.clearData()
                return true
            }

        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.searchMovies(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun renderMovieList(list: List<MovieModel>) {
        adapter.addData(list)
    }

    override fun renderMoreMovies(list: List<MovieModel>) {
        adapter.addMoreToEnd(list)
    }

    override fun renderFoundMovieList(list: List<MovieModel>) {
        adapterSearch.clearData()
        adapterSearch.addData(list)
    }

    override fun renderMoreFoundMovies(list: List<MovieModel>) {
        adapterSearch.addMoreToEnd(list)
    }

    override fun showLoading() {
        viewRoot.isEnabled = true
        viewRoot.isRefreshing = true
    }

    override fun hideLoading() {
        viewRoot.isRefreshing = false
        viewRoot.isEnabled = false
    }

    override fun onMoviewClick(movie: MovieModel) {
        listener?.navigateToDetails(movie.id)
    }

    override fun onBackOnline() {
        super.onBackOnline()
        if (presenter.isSearchEnable()) {
            if (adapterSearch.itemCount == 0) {
                presenter.getQuery()?.let { presenter.searchMovies(it) }
            }
        } else {
            if (adapter.itemCount == 0 && presenter.isAllReadyFetched) {
                presenter.isAllReadyFetched = false
                presenter.fetchUpcoming()
            }
        }
    }
}
