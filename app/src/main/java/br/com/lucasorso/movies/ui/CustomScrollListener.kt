package br.com.lucasorso.movies.ui

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class CustomScrollListener(
        var layoutManager: RecyclerView.LayoutManager) :
        RecyclerView.OnScrollListener() {

    private var isUseLinearLayoutManager: Boolean = false
    private var isUseGridLayoutManager: Boolean = false

    init {
        if (layoutManager is LinearLayoutManager) {
            isUseLinearLayoutManager = true
        } else if (layoutManager is GridLayoutManager) {
            isUseGridLayoutManager = true
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        var firstVisibleItemPosition: Int = -1

        if (isUseLinearLayoutManager) {
            firstVisibleItemPosition = (layoutManager as LinearLayoutManager)
                    .findFirstVisibleItemPosition()
        } else if (isUseGridLayoutManager) {
            firstVisibleItemPosition = (layoutManager as GridLayoutManager)
                    .findFirstVisibleItemPosition()
        }

        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >=
                    totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItens()
            }
        }
    }

    protected abstract fun loadMoreItens()

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

}