package com.nex3z.examples.trykotlin

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nex3z.examples.trykotlin.data.entity.MovieEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    var onItemClickListener: ((MovieEntity) -> Unit)? = null

    var movies: List<MovieEntity> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(viewGroup?.context)
        val itemView: View = inflater.inflate(R.layout.item_movie, viewGroup, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        if (viewHolder == null) {
            return
        }
        val movie: MovieEntity = movies[position]
        viewHolder.itemView.tv_name.text = movie.title
        if (movie.poster_path != null) {
            val url = movie.getPosterImageUrl(MovieEntity.PosterSize.W185)
            Picasso.with(viewHolder.itemView.context).load(url).into(viewHolder.itemView.iv_poster)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(movies[adapterPosition])
            }
        }
    }

}