package ru.example.kolsatest.presentation.filmlist

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.example.kolsatest.R
import ru.example.kolsatest.databinding.ListItemFilmBinding
import ru.example.kolsatest.domain.model.Film

class FilmHolder(
    private val binding: ListItemFilmBinding,
    private val onFilmClick: (Film) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
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
        Glide.with(binding.root)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(R.drawable.placeholder_image)
            .placeholder(R.drawable.placeholder_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .thumbnail(
                Glide.with(binding.root)
                    .load(imageUrl)
                    .override(80, 80)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(imageView)
    }
}