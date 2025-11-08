package ru.example.kolsatest.presentation.mapper

import okhttp3.internal.toImmutableList
import ru.example.kolsatest.domain.model.Film
import ru.example.kolsatest.presentation.filmlist.MainListItem

object FilmListMapperImpl : FilmListMapper {
    override fun map(
        genresTitle: String,
        genres: List<String>,
        selectedGenre: String?,
        filmsTitle: String,
        films: List<Film>?
    ): List<MainListItem> {
        return mutableListOf<MainListItem>().apply {
            add(MainListItem.HeaderItem(text = genresTitle))

            genres.forEach {
                add(
                    MainListItem.GenreItem(
                        name = it,
                        isSelected = it.equals(other = selectedGenre, ignoreCase = true)
                    )
                )
            }

            add(MainListItem.HeaderItem(text = filmsTitle))

            films?.let {
                addAll(it.map { film: Film ->
                    MainListItem.FilmItem(film)
                })
            }
        }.toImmutableList()
    }
}