package ru.example.kolsatest.presentation.mapper

import ru.example.kolsatest.domain.model.Film
import ru.example.kolsatest.presentation.MainListItem

interface FilmListMapper {
    fun map(
        genresTitle: String,
        genres: List<String>,
        selectedGenre: String?,
        filmsTitle: String,
        films: List<Film>?,
    ): List<MainListItem>
}