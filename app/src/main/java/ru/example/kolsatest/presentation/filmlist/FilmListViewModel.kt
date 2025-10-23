package ru.example.kolsatest.presentation.filmlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.example.kolsatest.domain.model.Film
import ru.example.kolsatest.domain.repository.FilmRepository
import javax.inject.Inject

private const val TAG = "FilmListViewModel"

@HiltViewModel
class FilmListViewModel @Inject constructor(
    private val repository: FilmRepository,
) : ViewModel() {
    private val _films: MutableStateFlow<FilmState> = MutableStateFlow(FilmState.Loading())
    val films: StateFlow<FilmState> = _films.asStateFlow()

    private val _genres = MutableStateFlow<List<String>>(emptyList())
    val genres = _genres.asStateFlow()

    private val _filmsOriginal = mutableListOf<Film>()
    var currentFilter: String? = null
        private set
    var selectedPosition = -1
    var scrollPosition = 0

    init {
        loadFilms()
    }

    fun loadFilms() {
        viewModelScope.launch {
            try {
                _films.value = FilmState.Loading()
                val result = repository.getFilms()
                when {
                    result.isSuccess -> {
                        val data = result.getOrDefault(emptyList()).sortedBy { it.localizedName }
                        _filmsOriginal.clear()
                        _filmsOriginal.addAll(data)
                        _films.value = FilmState.Success(data)
                        setupGenres(data)
                    }

                    result.isFailure -> {
                        val error = result.exceptionOrNull()?.message
                        _films.value = FilmState.Error(error)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in loadFilms", e)
            }
        }
    }

    private fun setupGenres(films: List<Film>) {
        val set = mutableSetOf<String>()
        films.forEach {
            it.genres.isNotEmpty().apply { set.addAll(it.genres) }
        }
        _genres.value = set.toList().sortedBy { it }
    }

    fun filtering(genre: String, pos: Int) {
        try {
            selectedPosition = pos
            if (genre.isNotEmpty()) {
                currentFilter = genre
                val filmsByFilter = _filmsOriginal.filter { it.genres.contains(genre) }
                _films.value = if (filmsByFilter.isNotEmpty()) {
                    FilmState.Success(filmsByFilter)
                } else
                    FilmState.Empty()
            } else {
                _films.value = FilmState.Success(_filmsOriginal)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in filtering", e)
        }
    }
}