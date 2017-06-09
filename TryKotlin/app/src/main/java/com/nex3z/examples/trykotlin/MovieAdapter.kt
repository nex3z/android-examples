package com.nex3z.examples.trykotlin

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nex3z.examples.trykotlin.data.entity.MovieEntity

import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter() : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    var movies: List<MovieEntity> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, position: Int): ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(viewGroup?.context);
        val itemView : View = inflater.inflate(R.layout.item_movie, viewGroup, false);
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        val movie : MovieEntity? = movies.get(position);
        if (viewHolder != null) {
            viewHolder.tvName.text = movie?.title
            viewHolder.tvOverview.text = movie?.overview
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.tv_name
        val tvOverview: TextView = itemView.tv_overview
    }
}