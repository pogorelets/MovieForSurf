package ru.lostpolygons.movieapplication.presentation

import dagger.Module
import dagger.Provides
import ru.lostpolygons.movieapplication.domain.FilterMovie
import ru.lostpolygons.movieapplication.domain.GetMovie
import javax.inject.Singleton

@Module
class MovieModule {
    //TODO Если в программе несколько экранов, то желательно делать не
    //Singlton, а добавлять ещё один @Scope, чтобы презентер мог пережить
    //активити, но не висел , если он совсем не нужен
    //В нашем случае один экран и презентер к нему будет нужен на всё время жизни приложения
    //поэтому можно сделать @Singlton
    @Provides
    @Singleton
    fun provideMoviePresenter(
        getMovie: GetMovie,
        filterMovie: FilterMovie
    ): MoviePresenter {
        return MoviePresenter(getMovie, filterMovie)
    }
}