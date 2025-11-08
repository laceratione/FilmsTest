package ru.example.kolsatest.presentation.filmlist

import androidx.recyclerview.widget.DiffUtil
import ru.example.kolsatest.presentation.filmlist.MainListItem.HeaderItem

class HeaderDiffUtil : DiffUtil.ItemCallback<HeaderItem>() {
    override fun areItemsTheSame(
        oldItem: HeaderItem,
        newItem: HeaderItem
    ): Boolean {
        return oldItem.text == newItem.text
    }

    override fun areContentsTheSame(
        oldItem: HeaderItem,
        newItem: HeaderItem
    ): Boolean {
        return oldItem == newItem
    }
}