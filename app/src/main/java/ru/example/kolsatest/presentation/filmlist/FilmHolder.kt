package ru.example.kolsatest.presentation.filmlist

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.example.kolsatest.R
import ru.example.kolsatest.databinding.ListItemFilmBinding
import ru.example.kolsatest.domain.model.Film
//import ru.example.kolsatest.presentation.filmlist.TAG

class FilmHolder(binding: ListItemFilmBinding, private val onFilmClick: (Film) -> Unit) : RecyclerView.ViewHolder(binding.root) {
    private val context = itemView.context
    private val imageView: ImageView = binding.imageView
    private val titleTextView: TextView = binding.tvTitle

    fun bind(film: Film) {
        loadImage(film.imageUrl)
        titleTextView.setText(film.localizedName)

        itemView.setOnClickListener {
            onFilmClick(film)
        }
    }

    private fun loadImage(imageUrl: String?) {
        Glide.with(context)
            .load(imageUrl)
            .sizeMultiplier(0.6f)
            .error(R.drawable.placeholder_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }
}