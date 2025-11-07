package ru.example.kolsatest.presentation.filmlist

import ru.example.kolsatest.domain.model.Film
import ru.example.kolsatest.presentation.MainListItem

sealed class FilmState {
    class Empty : FilmState()
    class Loading : FilmState()
    data class Error(val message: String?) : FilmState()
    data class Success(val films: List<Film>) : FilmState()
}