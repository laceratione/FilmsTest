package ru.example.kolsatest.presentation.filmlist

import androidx.recyclerview.widget.DiffUtil
import ru.example.kolsatest.presentation.filmlist.MainListItem.FilmItem

class FilmDiffUtil : DiffUtil.ItemCallback<FilmItem>() {
    override fun areItemsTheSame(
        oldItem: FilmItem,
        newItem: FilmItem
    ): Boolean {
        return oldItem.film.id == newItem.film.id
    }

    override fun areContentsTheSame(
        oldItem: FilmItem,
        newItem: FilmItem
    ): Boolean {
        return oldItem == newItem
    }
}