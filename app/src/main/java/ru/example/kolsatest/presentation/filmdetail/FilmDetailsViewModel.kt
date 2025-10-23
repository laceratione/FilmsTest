package ru.example.kolsatest.presentation.filmdetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.example.kolsatest.domain.model.Film
import ru.example.kolsatest.domain.repository.FilmRepository
import javax.inject.Inject

private const val TAG = "FilmDetailsViewModel"

@HiltViewModel
class FilmDetailsViewModel @Inject constructor(
    private val repository: FilmRepository,
) : ViewModel() {
    var film: Film? = null
        private set

    private val ratingFormat = "%.1f"

    fun setWorkout(film: Film) {
        this.film = film
    }

    fun getRating(rating: Float?): String? {
        return rating?.let { ratingFormat.format(it).replaceFirst(",", ".") }
    }
}