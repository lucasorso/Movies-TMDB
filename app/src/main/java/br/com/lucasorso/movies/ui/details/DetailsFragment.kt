package br.com.lucasorso.movies.ui.details

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import br.com.lucasorso.movies.R
import br.com.lucasorso.movies.domain.model.MovieModel
import br.com.lucasorso.movies.ui.BaseFragment
import br.com.lucasorso.movies.ui.HasPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class DetailsFragment : BaseFragment(),
        DetailsContract,
        HasPresenter<DetailsPresenter> {

    private var progress: ProgressBar? = null

    override val presenter = DetailsPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.setContract(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress = view.findViewById(R.id.progressBar)
        initPresenter()
    }

    private fun initPresenter() {
        arguments?.let { presenter.init(it.getInt("id")) }
    }

    override fun onDetailsReceive(movie: MovieModel) {
        view?.findViewById<TextView>(R.id.movieTitle)?.text = movie.title

        view?.findViewById<TextView>(R.id.movieGenres)
                ?.text = getString(R.string.movie_genres_param,
                TextUtils.join(", ", movie.genresList))

        view?.findViewById<TextView>(R.id.releaseDate)
                ?.text = getString(R.string.release_date_param, movie.releaseDate)

        view?.findViewById<TextView>(R.id.movieOverview)
                ?.text = getString(R.string.movie_overview_param, movie.overView)

        activity?.let { context ->
            view?.findViewById<ImageView>(R.id.moviePoster)?.let { imageView ->
                Glide.with(context)
                        .load(movie.posterPath)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade(350))
                        .apply(RequestOptions.errorOf(R.drawable.no_image_place_holder))
                        .into(imageView)
            }

            view?.findViewById<ImageView>(R.id.movieBackdrop)?.let { imageView ->
                Glide.with(context)
                        .load(movie.backdropPath)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade(350))
                        .apply(RequestOptions.errorOf(R.drawable.no_image_place_holder))
                        .into(imageView)
            }
        }
    }

    override fun showLoading() {
        progress?.visibility = VISIBLE
    }

    override fun hideLoading() {
        progress?.visibility = GONE
    }

    override fun onBackOnline() {
        super.onBackOnline()
        initPresenter()
    }

}