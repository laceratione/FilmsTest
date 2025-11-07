package ru.example.kolsatest.presentation.filmlist

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.example.kolsatest.databinding.ListItemHeaderBinding

class HeaderHolder(private val binding: ListItemHeaderBinding) : ViewHolder(binding.root) {
    fun bind(text: String) {
        binding.tvHeader.text = text
    }
}