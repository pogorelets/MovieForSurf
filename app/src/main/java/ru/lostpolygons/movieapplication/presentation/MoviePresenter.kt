package ru.lostpolygons.movieapplication.presentation

import android.content.res.Configuration
import android.view.View

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.lostpolygons.movieapplication.base.BasePresenter
import ru.lostpolygons.movieapplication.domain.FilterMovie
import ru.lostpolygons.movieapplication.domain.GetMovie
import ru.lostpolygons.movieapplication.model.ItemMovie
import javax.inject.Inject

class MoviePresenter @Inject constructor(
    private val getMovie: GetMovie,
    private val filterMovie: FilterMovie
) : BasePresenter<MovieContract.View>(), MovieContract.Presenter {

    private var currentPage = 0
    private var totalPages = 0
    private var filterPage = 0
    private var totalFilterPage = 0
    private val listMovies: MutableList<ItemMovie> = mutableListOf()
    private var listFilterResult: List<ItemMovie> = listOf()
    private var stateNotFound = View.GONE
    private var stateErrorRequest = View.GONE
    private var stateFilterSearch = View.GONE
    private var isLoading = false
    private var isFilterLoading = false
    private var firstPosition = 0
    private var firstFilterPosition = 0
    private var configuration = 0
    private var query = ""
    private var listFavorites: MutableList<Int> = mutableListOf()

    override fun start() { if (currentPage == 0) { getMovies(currentPage + 1) } }

    override fun setFavorite(list: MutableList<Int>){ listFavorites = list }

    override fun attach(view: MovieContract.View) {
        super.attach(view)

        if (currentPage == 0) view.showProgress()
        setOrientationView(configuration)
        when {
            stateNotFound == View.VISIBLE -> {
                view.setStateNotFound(View.VISIBLE)
                view.visibleSearchClear()
            }
            stateErrorRequest == View.VISIBLE -> view.setStateErrorRequest(View.VISIBLE)
            listFilterResult.isNotEmpty() -> {
                view.showFilterResults(listFilterResult,listFavorites)
                view.visibleSearchClear()
                view.setListPosition(firstFilterPosition)
            }
            listMovies.isNotEmpty() -> {
                view.showFilterResults(listMovies,listFavorites)
                view.setListPosition(firstPosition)
            }
        }
    }

    override fun setConfiguration(config: Int){ configuration = config }

    override fun setOrientationView(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) view?.setPortraitView()
        else view?.setLandscapeView()
    }

    override fun clearSearch() {
        query = ""
        firstFilterPosition = 0
        listFilterResult = listOf()
        view?.showFilterResults(listMovies,listFavorites)
        view?.goneSearchClear()
    }

    override fun pullRefresh(){
        view?.clearList()
        if (listFilterResult.isNotEmpty())pullRefreshFilterList()
        else pullRefreshAllMovieList()
    }

    private  fun pullRefreshFilterList(){
        filterPage = 0
        totalFilterPage = 0
        firstFilterPosition = 0
        totalPages = 0
        listFilterResult = listOf()
        filterMovies(query)
    }

    private fun pullRefreshAllMovieList(){
        currentPage = 0
        totalPages = 0
        firstPosition = 0
        firstFilterPosition = 0
        listMovies.clear()
        getMovies(currentPage + 1)
    }

    private fun getMovies(page: Int) {
        if (currentPage == 0) view?.showProgress()
        isLoading = true
        disposables += getMovie.execute(GetMovie.Params(page))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (currentPage == 0) view?.dismissProgress()
                listMovies.addAll(it.results)
                currentPage = it.page
                totalPages = it.totalPages
                view?.updateAdapter(it.results,listFavorites)
                isLoading = false
                view?.hideLoadingFooter()

            },
                {
                    view?.hideLoadingFooter()
                    if (currentPage == 0) {
                        view?.dismissProgress()
                        view?.setStateErrorRequest(View.VISIBLE)
                        stateErrorRequest = View.VISIBLE
                    } else view?.setRepeatFooter()

                    lastOperation = Runnable { getMovies(currentPage) }
                    isLoading = false
                })

    }

    override fun setStateNotFound(visibility: Int) {
        stateNotFound = visibility
        view?.setStateNotFound(visibility)
    }

    override fun setStateErrorRequest(visibility: Int) {
        stateErrorRequest = visibility
        view?.setStateErrorRequest(visibility)
    }

    override fun retryRequest() {
        view?.hideRepeatFooter()
        lastOperation?.run()
    }

    override fun filterMovies(query: String) {
        this.query = query
        isFilterLoading = true
        view?.setStateFilterSearch(View.VISIBLE)
        stateFilterSearch = View.VISIBLE
        disposables += filterMovie.execute(FilterMovie.Params(filterPage + 1, query))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                isFilterLoading = false
                filterPage = it.page
                totalFilterPage = it.totalPages
                listFilterResult = it.results
                view?.showFilterResults(it.results,listFavorites)
                view?.visibleSearchClear()
                view?.setStateFilterSearch(View.GONE)
                stateFilterSearch = View.GONE
                if (it.results.isEmpty()) {
                    view?.setStateNotFound(View.VISIBLE)
                    stateNotFound = View.VISIBLE
                }
            },
                {
                    isFilterLoading = false
                    if (currentPage == 0) view?.setStateErrorRequest(View.VISIBLE)
                    else view?.setRepeatFooter()
                    stateErrorRequest = View.VISIBLE
                    lastOperation = Runnable { filterMovies(query) }
                    view?.setStateFilterSearch(View.GONE)
                    stateFilterSearch = View.GONE
                })
    }

    override fun paging(visibleItemCount: Int, totalItemCount: Int, firstVisibleItemPosition: Int) {
        if (listFilterResult.isNotEmpty()){ pagingFilterList(visibleItemCount,totalItemCount,firstVisibleItemPosition)
        } else pagingAllMovieList(visibleItemCount, totalItemCount, firstVisibleItemPosition)
    }

    private fun pagingFilterList(visibleItemCount: Int, totalItemCount: Int, firstVisibleItemPosition: Int){
        if (!isFilterLoading && filterPage != totalFilterPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >=
                totalItemCount && firstVisibleItemPosition >= 0) {
                view?.showLoadingFooter()
                filterMovies(query)
            }
        }
    }

    private fun pagingAllMovieList(visibleItemCount: Int, totalItemCount: Int, firstVisibleItemPosition: Int){
        if (!isLoading && currentPage != totalPages) {
            if ((visibleItemCount + firstVisibleItemPosition) >=
                totalItemCount && firstVisibleItemPosition >= 0) {
                view?.showLoadingFooter()
                getMovies(currentPage+1)
            }
        }
    }

    fun saveListMoviePosition(firstVisibleItemPosition: Int){
        if (listFilterResult.isNotEmpty()) firstFilterPosition = firstVisibleItemPosition
        else firstPosition = firstVisibleItemPosition
    }
}