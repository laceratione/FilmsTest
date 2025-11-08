package ru.example.kolsatest.presentation.filmlist

import androidx.recyclerview.widget.DiffUtil
import ru.example.kolsatest.presentation.filmlist.MainListItem.GenreItem

class GenreDiffUtil : DiffUtil.ItemCallback<GenreItem>() {
    override fun areItemsTheSame(
        oldItem: GenreItem,
        newItem: GenreItem
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: GenreItem,
        newItem: GenreItem
    ): Boolean {
        return oldItem == newItem
    }
}