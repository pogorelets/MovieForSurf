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
    private var filterPage = 1
    private val listMovies: MutableList<ItemMovie> = mutableListOf()
    private var listFilterResult: List<ItemMovie> = listOf()
    private var stateNotFound = View.GONE
    private var stateErrorRequest = View.GONE
    private var stateFilterSearch = View.GONE
    private var isLoading = false
    private var firstPosition = 0
    private var configuration = 0

    override fun start() {
        if (currentPage == 0) getMovies(currentPage + 1)
    }

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
                view.showFilterResults(listFilterResult)
                view.visibleSearchClear()
            }
            listMovies.isNotEmpty() -> {
                view.updateAdapter(listMovies)
                view.setListPosition(firstPosition)
            }
        }

    }

    fun setConfiguration(config: Int){ configuration = config }

    override fun setOrientationView(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) view?.setPortraitView()
        else view?.setLandscapeView()
    }

    override fun clearSearch() {
        listFilterResult = listOf()
        view?.showFilterResults(listMovies)
        view?.goneSearchClear()

    }

    override fun getMovies(page: Int) {
        isLoading = true
        disposables += getMovie.execute(GetMovie.Params(page))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (currentPage == 0) view?.dismissProgress()
                listMovies.addAll(it.results)
                currentPage = it.page
                totalPages = it.totalPages
                view?.updateAdapter(it.results)
                isLoading = false
                view?.hideLoadingFooter()

            },
                {
                    view?.hideLoadingFooter()
                    view?.setStateErrorRequest(View.VISIBLE)
                    stateErrorRequest = View.VISIBLE
                    lastOperation = Runnable { getMovies(currentPage) }
                    isLoading = false
                })

    }

    fun setStateNotFound(visibility: Int) {
        stateNotFound = visibility
        view?.setStateNotFound(visibility)
    }

    fun setStateErrorRequest(visibility: Int) {
        stateErrorRequest = visibility
        view?.setStateErrorRequest(visibility)
    }

    fun retryRequest() {
        lastOperation?.run()
    }

    override fun filterMovies(query: String) {
        view?.setStateFilterSearch(View.VISIBLE)
        stateFilterSearch = View.VISIBLE
        disposables += filterMovie.execute(FilterMovie.Params(filterPage, query))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                listFilterResult = it.results
                view?.showFilterResults(it.results)
                view?.visibleSearchClear()
                view?.setStateFilterSearch(View.GONE)
                stateFilterSearch = View.GONE
                if (it.results.isEmpty()) {
                    view?.setStateNotFound(View.VISIBLE)
                    stateNotFound = View.VISIBLE
                }
            },
                {
                    view?.setStateErrorRequest(View.VISIBLE)
                    stateErrorRequest = View.VISIBLE
                    lastOperation = Runnable { filterMovies(query) }
                    view?.setStateFilterSearch(View.GONE)
                    stateFilterSearch = View.GONE
                })
    }

    fun pagingListMovies(visibleItemCount: Int, totalItemCount: Int, firstVisibleItemPosition: Int) {
        if (!isLoading && currentPage != totalPages) {
            if ((visibleItemCount + firstVisibleItemPosition) >=
                totalItemCount && firstVisibleItemPosition >= 0) {
                view?.showLoadingFooter()
                getMovies(currentPage+1)
            }
        }
    }

    fun saveListMoviePosition(firstVisibleItemPosition: Int){
        firstPosition = firstVisibleItemPosition
    }
}