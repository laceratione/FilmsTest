package ru.example.kolsatest.presentation.filmlist

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.example.kolsatest.R
import ru.example.kolsatest.databinding.ListItemGenreBinding

class GenreHolder(binding: ListItemGenreBinding, private val onGenreClick: (String) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    private val context = itemView.context
    private val genreTextView: TextView = binding.tvGenre

    init {
        itemView.background = ContextCompat.getDrawable(context, R.drawable.genre_item_selector)
    }

    fun bind(genre: String, isSelected: Boolean) {
        genreTextView.setText(genre.replaceFirstChar { it.uppercase() })
        itemView.isSelected = isSelected
        itemView.setOnClickListener{
            onGenreClick(genre)
        }
    }
}