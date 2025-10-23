package ru.example.kolsatest.presentation.filmdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.example.kolsatest.R
import ru.example.kolsatest.databinding.FragmentFilmDetailsBinding
import ru.example.kolsatest.domain.model.Film

private const val TAG = "FilmDetailsFragment"

@AndroidEntryPoint
class FilmDetailsFragment : Fragment() {
    private val viewModel: FilmDetailsViewModel by viewModels()
    private val args: FilmDetailsFragmentArgs by navArgs()
    private var binding: FragmentFilmDetailsBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setWorkout(args.film)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilmDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        try {
            binding?.apply {
                viewModel.film?.let {
                    loadImage(it, imageView)
                    tvTitle.setText(it.localizedName)
                    tvGenreAndYear.setText(getGenreAndYear(it))

                    val sRating = viewModel.getRating(it.rating)
                    sRating?.let { tvRatingValue.setText(getString(R.string.rating_format, sRating)) } ?: run {
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

    private fun loadImage(film: Film, imageView: ImageView) {
        Glide.with(requireContext())
            .load(film.imageUrl)
            .error(R.drawable.placeholder_image)
            .centerCrop()
            .into(imageView)
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