package ru.example.kolsatest.presentation

import ru.example.kolsatest.domain.model.Film

sealed class MainListItem {
    data class HeaderItem(val text: String) : MainListItem()
    data class GenreItem(val name: String, val isSelected: Boolean) : MainListItem()
    data class FilmItem(val film: Film) : MainListItem()
}