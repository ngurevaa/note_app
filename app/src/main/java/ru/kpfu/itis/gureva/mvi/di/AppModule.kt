package ru.kpfu.itis.gureva.mvi.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Provide for instances of FlockApplication that will be used
     * to inject context in VaccinationViewModel
     */
    @Singleton
    @Provides
    fun provideBaseApplication(@ApplicationContext context: Context): App {
        return context as App
    }

    @Provides
    fun provideName() = "nana"
}
