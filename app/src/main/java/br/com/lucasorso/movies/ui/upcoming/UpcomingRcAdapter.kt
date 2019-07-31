package br.com.lucasorso.movies.ui.upcoming

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.lucasorso.movies.R
import br.com.lucasorso.movies.domain.model.MovieModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlin.reflect.KFunction1

class UpcomingRcAdapter(val data: MutableList<MovieModel>,
                        val onMovieClick: KFunction1<MovieModel, Unit>) :
        RecyclerView.Adapter<UpcomingRcAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = data[position]
        holder.title.text = item.title
        holder.releaseDate.text = item.releaseDate
        Glide.with(holder.itemView.context)
                .load(item.posterPath)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(350))
                .apply(RequestOptions.errorOf(R.drawable.no_image_place_holder))
                .into(holder.moviePoster)
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    fun addData(list: List<MovieModel>) {
        notifyDataSetChanged()
        data.addAll(itemCount, list)
    }

    fun addMoreToEnd(list: List<MovieModel>) {
        data.addAll(itemCount, list)
        notifyItemRangeInserted(itemCount, list.size)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moviePoster: AppCompatImageView = itemView.moviePoster
        val title: TextView = itemView.title
        val releaseDate: TextView = itemView.releaseDate

        init {
            itemView.setOnClickListener {
                onMovieClick(data[adapterPosition])
            }
        }
    }
}

fun <E> List<E>.lastItem(): Int {
    return this.size - 1
}
