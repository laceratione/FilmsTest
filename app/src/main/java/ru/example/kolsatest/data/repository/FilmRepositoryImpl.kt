package ru.example.kolsatest.data.repository

import ru.example.kolsatest.data.api.FilmsApi
import ru.example.kolsatest.data.model.mapToDomain
import ru.example.kolsatest.domain.model.Film
import ru.example.kolsatest.domain.repository.FilmRepository
import javax.inject.Inject

class FilmRepositoryImpl @Inject constructor(
    private var filmsApi: FilmsApi,
) : FilmRepository {
    override suspend fun getFilms(): Result<List<Film>> {
        return try {
            val films = filmsApi.getFilms().mapToDomain()
            Result.success(films)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}