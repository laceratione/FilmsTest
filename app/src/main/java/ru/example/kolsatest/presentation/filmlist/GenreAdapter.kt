package ru.example.kolsatest.presentation.filmlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.example.kolsatest.R

private const val TAG = "GenreAdapter"

class GenreAdapter(
    private val onItemClick: (String, Int) -> Unit,
    initSelectedPosition: Int,
) : RecyclerView.Adapter<GenreAdapter.GenreHolder>() {
    private val genres = mutableListOf<String>()
    private var selectedPosition = initSelectedPosition

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenreAdapter.GenreHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_genre, parent, false)
        return GenreHolder(view)
    }

    override fun onBindViewHolder(holder: GenreAdapter.GenreHolder, position: Int) {
        val item = genres.get(position)
        holder.bind(item, position)
    }

    override fun getItemCount() = genres.size

    fun updateGenres(newGenres: List<String>) {
        genres.clear()
        genres.addAll(newGenres)
        notifyDataSetChanged()
    }

    inner class GenreHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val context = itemView.context
        private val genreTextView: TextView = view.findViewById(R.id.tvGenre)

        init {
            itemView.background = ContextCompat.getDrawable(context, R.drawable.genre_item_selector)
        }

        fun bind(genre: String, position: Int) {
            genreTextView.setText(genre.replaceFirstChar { it.uppercase() })
            itemView.isSelected = position == selectedPosition

            itemView.setOnClickListener {
                val newSelectedPosition = if (position == selectedPosition) -1 else position
                updateSelection(newSelectedPosition, genre)
            }
        }

        private fun updateSelection(newPosition: Int, genre: String) {
            val oldPosition = selectedPosition
            selectedPosition = newPosition

            if (oldPosition != -1) notifyItemChanged(oldPosition)
            if (newPosition != -1) notifyItemChanged(newPosition)

            val selectedGenre = if (newPosition != -1) genre else ""
            onItemClick(selectedGenre, newPosition)
        }
    }
}