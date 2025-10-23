package ru.example.kolsatest.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.example.kolsatest.data.repository.FilmRepositoryImpl
import ru.example.kolsatest.domain.repository.FilmRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindFilmRepositoryImpl(filmRepositoryImpl: FilmRepositoryImpl): FilmRepository
}