package ru.example.kolsatest.domain.repository

import ru.example.kolsatest.domain.model.Film

interface FilmRepository {
    suspend fun getFilms(): Result<List<Film>>
}