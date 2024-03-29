package ru.lostpolygons.movieapplication.presentation
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_request.*
import kotlinx.android.synthetic.main.message_not_found.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.apptimizm.formatfit.di.preferences.PreferenceProvider
import ru.lostpolygons.movieapplication.R
import ru.lostpolygons.movieapplication.base.BaseActivity
import ru.lostpolygons.movieapplication.model.ItemMovie
import javax.inject.Inject

class MainActivity : BaseActivity(),MovieContract.View , MovieAdapter.MovieClickListener{

    //TODO по поводу адаптации вёрстки под разные размеры
    //как пример создан ресурс sw360dp-land

    @Inject
    lateinit var presenter: MoviePresenter

    @Inject
    lateinit var prefs : PreferenceProvider
    private val adapter = MovieAdapter(this)

    override fun setStateErrorRequest(visibility: Int) { groupErrorRequest.visibility = visibility }

    override fun setStateNotFound(visibility: Int) {
        groupNotFound.visibility = visibility
        if  (groupNotFound.visibility == View.VISIBLE){
            val mes = """${getString(R.string.for_request)}${nameForSearch.text}${getString(R.string.not_found)}"""
            messageNotFound.text = mes
        }
    }

    override fun clearList() { adapter.clearList() }

    override fun showLoadingFooter() {adapter.showFooter() }

    override fun hideLoadingFooter() {adapter.removeFooter() }

    override fun setStateFilterSearch(visibility: Int) { progressFilter.visibility = visibility }

    override fun showProgress() { progressBar.visibility = View.VISIBLE }

    override fun dismissProgress() { progressBar.visibility = View.GONE }

    override fun visibleSearchClear() { groupClear.visibility = View.VISIBLE }

    override fun setListPosition(position: Int) { listMovie.scrollToPosition(position) }

    override fun goneSearchClear() {
        groupClear.visibility = View.GONE
        nameForSearch.text.clear()
        presenter.setStateNotFound(View.GONE)
    }

    override fun setLandscapeView() { listMovie.layoutManager = GridLayoutManager(this,2) }

    override fun setFavorite(item: ItemMovie, isFavorite: Boolean) {
        if (isFavorite) prefs.saveFavorite(item.id)
        else prefs.removeFavorite(item.id)
        presenter.setFavorite(prefs.getFavorite())
    }

    override fun setPortraitView() { listMovie.layoutManager = LinearLayoutManager(this) }

    override fun onMovieItemClick(item: ItemMovie) {
        Snackbar.make(listMovie, item.title, Snackbar.LENGTH_LONG).show()
        presenter.retryRequest()
    }

    override fun updateAdapter(list: List<ItemMovie>, favorites: List<Int>) { adapter.swapData(list,favorites) }

    override fun setRepeatFooter() { adapter.showFooterRepeat() }

    override fun hideRepeatFooter() { adapter.removeRepeatFooter() }

    override fun showFilterResults(list: List<ItemMovie>, favorites: List<Int>) { adapter.setFilterData(list, favorites) }

    override fun repeatClick() { presenter.retryRequest()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)
        presenter.setFavorite(prefs.getFavorite())

        listMovie.adapter = adapter
        presenter.start()

        btnSearch.setOnClickListener {
            if (nameForSearch.text.isNotEmpty()){
                presenter.filterMovies(nameForSearch.text.toString())
                presenter.setStateErrorRequest(View.GONE)
            }
        }

        clearSearch.setOnClickListener {
            presenter.clearSearch()
            presenter.setStateNotFound(View.GONE)
        }

        btnRepeat.setOnClickListener { presenter.retryRequest() }
        swiperefresh.setOnRefreshListener {
            presenter.pullRefresh()
            swiperefresh.isRefreshing = false }
        
        listMovie.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0){
                    val layout = listMovie.layoutManager as LinearLayoutManager
                    val visibleItemCount = layout.childCount
                    val totalItemCount = layout.itemCount
                    val firstVisibleItemPosition = layout.findFirstVisibleItemPosition()
                    presenter.paging(visibleItemCount, totalItemCount, firstVisibleItemPosition)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        presenter.setConfiguration(this.resources.configuration.orientation)
        presenter.attach(this)
    }

    override fun onPause() {
        super.onPause()
        val layout = listMovie.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layout.findFirstVisibleItemPosition()
        presenter.saveListMoviePosition(firstVisibleItemPosition)
        presenter.detach()
    }


}
