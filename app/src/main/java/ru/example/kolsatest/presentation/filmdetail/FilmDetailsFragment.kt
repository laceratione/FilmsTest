package ru.example.kolsatest.presentation.filmdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import ru.example.kolsatest.R
import ru.example.kolsatest.databinding.FragmentFilmDetailsBinding
import ru.example.kolsatest.domain.model.Film

private const val TAG = "FilmDetailsFragment"

@AndroidEntryPoint
class FilmDetailsFragment : Fragment() {
    private val args: FilmDetailsFragmentArgs by navArgs()
    private var _binding: FragmentFilmDetailsBinding? = null
    private val binding get() = _binding!!
    private var film: Film? = null
    private val ratingFormat = "%.1f"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        film = args.film
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilmDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        try {
            binding.apply {
                film?.let {
                    loadImage(it.imageUrl, imageView)
                    tvTitle.setText(it.localizedName)
                    tvGenreAndYear.setText(getGenreAndYear(it))

                    val sRating = getRating(it.rating)
                    sRating?.let {
                        tvRatingValue.setText(
                            getString(
                                R.string.rating_format,
                                sRating
                            )
                        )
                    } ?: run {
                        llRating.visibility = View.GONE
                    }
                    it.description?.let { desc -> tvDescription.setText(desc) } ?: run {
                        tvDescription.visibility = View.GONE
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in setupView", e)
        }
    }

    private fun loadImage(imageUrl: String?, imageView: ImageView) {
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

    private fun getRating(rating: Float?): String? {
        return rating?.let { ratingFormat.format(it).replaceFirst(",", ".") }
    }

    private fun getGenreAndYear(film: Film): String {
        val genresText = film.genres.joinToString(", ")
        val yearText = film.year

        return if (genresText.isNotEmpty()) {
            getString(R.string.genre_year_format, genresText, yearText)
        } else {
            getString(R.string.just_year_format, yearText)
        }
    }
}