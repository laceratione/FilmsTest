package ru.example.kolsatest.presentation.filmlist

import ru.example.kolsatest.domain.model.Film

sealed class FilmState {
    class Empty : FilmState()
    class Loading : FilmState()
    data class Error(val message: String?) : FilmState()
    data class Success(val films: List<Film>) : FilmState()
}