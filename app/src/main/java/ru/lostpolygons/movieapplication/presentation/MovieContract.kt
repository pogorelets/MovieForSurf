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
        fun setConfiguration(config: Int)
        fun setFavorite(list: MutableList<Int>)
        fun filterMovies(query: String)
        fun setOrientationView(orientation: Int)
        fun clearSearch()
        fun pullRefresh()
        fun setStateNotFound(visibility: Int)
        fun setStateErrorRequest(visibility: Int)
        fun retryRequest()
        fun paging(visibleItemCount: Int, totalItemCount: Int, firstVisibleItemPosition: Int)
    }
}