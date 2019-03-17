package ru.apptimizm.formatfit.di.preferences

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface PreferenceModule {
    @Binds
    @Singleton
    fun getDefaultInstance(preferenceProvider: AppPreferences): PreferenceProvider
}