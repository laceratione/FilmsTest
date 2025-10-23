package ru.example.kolsatest.presentation.filmlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.example.kolsatest.R
import ru.example.kolsatest.domain.model.Film

private const val TAG = "FilmAdapter"

class FilmAdapter(
    private val onItemClick: (Film) -> Unit,
): ListAdapter<Film, FilmAdapter.FilmHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilmAdapter.FilmHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_film, parent, false)
        return FilmHolder(view)
    }

    override fun onBindViewHolder(holder: FilmAdapter.FilmHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class FilmHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val context = itemView.context
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        private val titleTextView: TextView = view.findViewById(R.id.tv_title)

        fun bind(film: Film) {
            try {
                loadImage(film)
                titleTextView.setText(film.localizedName)

                itemView.setOnClickListener {
                    onItemClick(film)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in bind", e)
            }
        }

        private fun loadImage(film: Film) {
            Glide.with(context)
                .load(film.imageUrl)
                .error(R.drawable.placeholder_image)
                .into(imageView)
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem == newItem
        }
    }
}