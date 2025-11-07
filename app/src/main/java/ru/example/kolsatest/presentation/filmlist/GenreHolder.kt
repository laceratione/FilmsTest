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
        itemView.setOnClickListener({ onGenreClick(genre) })
//            itemView.isSelected = position == selectedPosition
//
//            itemView.setOnClickListener {
//                val newSelectedPosition = if (position == selectedPosition) -1 else position
//                updateSelection(newSelectedPosition, genre)
//            }
    }

//        private fun updateSelection(newPosition: Int, genre: String) {
//            val oldPosition = selectedPosition
//            selectedPosition = newPosition
//
//            if (oldPosition != -1) notifyItemChanged(oldPosition)
//            if (newPosition != -1) notifyItemChanged(newPosition)
//
//            val selectedGenre = if (newPosition != -1) genre else ""
//            onItemClick(selectedGenre, newPosition)
//        }
}