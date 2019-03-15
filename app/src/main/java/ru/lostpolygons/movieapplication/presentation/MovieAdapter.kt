package ru.lostpolygons.movieapplication.presentation

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_movie.view.*
import ru.lostpolygons.movieapplication.R
import ru.lostpolygons.movieapplication.model.ItemMovie
import java.text.SimpleDateFormat
import android.support.v7.widget.RecyclerView.ViewHolder
import ru.lostpolygons.movieapplication.presentation.MovieAdapter.FooterViewHolder




class MovieAdapter(private val listener: MovieClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: MutableList<ItemMovie> = mutableListOf()

    private val FOOTER_VIEW = 1

    interface MovieClickListener {
        fun onMovieItemClick(item: ItemMovie)
        fun setFavorite(item: ItemMovie, isFavorite: Boolean)
    }

    fun swapData(items: List<ItemMovie>) {
        this.data.addAll(items)
        notifyDataSetChanged()
    }

    fun setFilterData(items: List<ItemMovie>) {
        this.data = items as MutableList<ItemMovie>
        notifyDataSetChanged()
    }

    fun showFooter() {
        this.data.add(ItemMovie("FOOTER", "", "", "", ""))
        notifyDataSetChanged()
    }

    fun removeFooter(){
        val footer = this.data.find{item: ItemMovie -> item.title == "FOOTER"}
        this.data.remove(footer)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].title == "FOOTER") {
            FOOTER_VIEW
        } else super.getItemViewType(position)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType === FOOTER_VIEW) {
            return FooterViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_footer_loading,
                    parent,
                    false
                )
            )
        }

        return MovieHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
        )
    }



    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            if (holder is MovieHolder) {
                val vh = holder as MovieHolder
                vh.bind(data[position], listener)
            } else if (holder is FooterViewHolder) {
                val vh = holder
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val PATH = "https://image.tmdb.org/t/p/w185"
        fun bind(item: ItemMovie, listener: MovieClickListener) = with(itemView) {
            Glide.with(itemView.context)
                .load("$PATH${item.posterPath}")
                .into(itemView.imageMovie)
            itemView.title.text = item.title
            itemView.overview.text = item.overview
            itemView.date.text = getDate(item.releaseDate)
            itemView.setOnClickListener { listener.onMovieItemClick(item) }
            itemView.favorite.setOnClickListener {
                itemView.favorite.isSelected = !itemView.favorite.isSelected
                listener.setFavorite(item, itemView.favorite.isSelected)
            }
        }

        private fun getDate(date: String): String {
            val newDate = SimpleDateFormat("yyyy-MM-dd").parse(date)
            return SimpleDateFormat("dd MMMM yyyy").format(newDate.time)
        }
    }

    class FooterViewHolder(itemView: View) : ViewHolder(itemView)
}