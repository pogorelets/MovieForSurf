package ru.lostpolygons.movieapplication.presentation

import ru.lostpolygons.movieapplication.base.BaseContract
import ru.lostpolygons.movieapplication.model.ItemMovie

interface MovieContract {
    interface View: BaseContract.View{
        fun setLandscapeView()
        fun setPortraitView()
        fun updateAdapter(list: List<ItemMovie>, favorites: List<Int>)
        fun showFilterResults(list: List<ItemMovie>, favorites: List<Int>)
        fun visibleSearchClear()
        fun goneSearchClear()
        fun setStateNotFound(visibility: Int)
        fun setStateErrorRequest(visibility: Int)
        fun setStateFilterSearch(visibility: Int)
        fun showLoadingFooter()
        fun hideLoadingFooter()
        fun setRepeatFooter()
        fun hideRepeatFooter()
        fun setListPosition(position: Int)
        fun clearList()
    }
    interface Presenter: BaseContract.Presenter<MovieContract.View>{
        fun getMovies(page: Int)
        fun filterMovies(query: String)
        fun setOrientationView(orientation: Int)
        fun clearSearch()
    }
}