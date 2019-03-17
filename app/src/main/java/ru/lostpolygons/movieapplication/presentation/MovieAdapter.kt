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
import kotlinx.android.synthetic.main.error_request.view.*


class MovieAdapter(private val listener: MovieClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: MutableList<ItemMovie> = mutableListOf()
    private var favorites = listOf<Int>()
    private val FOOTER_VIEW = 1
    private val FOOTER_ERROR_VIEW = 2
    private val FOOTER = "FOOTER"
    private val FOOTER_ERROR = "FOOTER_ERROR"
    private val EMPTY =""

    interface MovieClickListener {
        fun onMovieItemClick(item: ItemMovie)
        fun setFavorite(item: ItemMovie, isFavorite: Boolean)
        fun repeatClick()
    }

    fun swapData(items: List<ItemMovie>, favorites: List<Int>) {
        this.data.addAll(items)
        this.favorites = favorites
        notifyDataSetChanged()
    }

    fun setFilterData(items: List<ItemMovie>, favorites: List<Int>) {
        this.data = items as MutableList<ItemMovie>
        this.favorites = favorites
        notifyDataSetChanged()
    }

    fun clearList(){
        this.data = mutableListOf()
        notifyDataSetChanged()
    }

    fun showFooter() {
        this.data.add(ItemMovie(0,FOOTER, EMPTY, EMPTY, EMPTY, EMPTY))
        notifyDataSetChanged()
    }

    fun showFooterRepeat(){
        this.data.add(ItemMovie(0,FOOTER_ERROR, EMPTY, EMPTY, EMPTY, EMPTY))
        notifyDataSetChanged()
    }

    fun removeFooter(){
        val footer = this.data.find{item: ItemMovie -> item.title == FOOTER}
        this.data.remove(footer)
        notifyDataSetChanged()
    }

    fun removeRepeatFooter(){
        val footer = this.data.find{item: ItemMovie -> item.title == FOOTER_ERROR}
        this.data.remove(footer)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            data[position].title == FOOTER -> FOOTER_VIEW
            data[position].title == FOOTER_ERROR -> FOOTER_ERROR_VIEW
            else -> super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            FOOTER_VIEW -> return FooterViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_footer_loading,
                    parent,
                    false
                )
            )
            FOOTER_ERROR_VIEW -> return FooterRepeatHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_footer_error,
                    parent,
                    false
                )
            )
            else -> return MovieHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_movie, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            when (holder) {
                is MovieHolder -> {
                    val vh = holder as MovieHolder
                    vh.bind(data[position], listener, favorites)
                }
                is FooterViewHolder -> { val vh = holder }
                is FooterRepeatHolder ->{
                    val vh = holder as FooterRepeatHolder
                    vh.bind(listener)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val PATH = "https://image.tmdb.org/t/p/w185"
        fun bind(item: ItemMovie, listener: MovieClickListener,favorites: List<Int> ) = with(itemView) {
            Glide.with(itemView.context)
                .load("$PATH${item.posterPath}")
                .into(itemView.imageMovie)

            itemView.favorite.isSelected = favorites.find {it == item.id} != null
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

    class FooterRepeatHolder(itemView: View) : ViewHolder(itemView){
        fun bind(listener: MovieClickListener){
            itemView.btnRepeat.setOnClickListener { listener.repeatClick() }
        }
    }
}