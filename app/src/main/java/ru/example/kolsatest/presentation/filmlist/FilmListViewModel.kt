package ru.example.kolsatest.presentation.filmlist

import android.os.Parcelable
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
import ru.example.kolsatest.presentation.mapper.FilmListMapper
import javax.inject.Inject

private const val TAG = "FilmListViewModel"

@HiltViewModel
class FilmListViewModel @Inject constructor(
    private val repository: FilmRepository,
) : ViewModel() {
    private val _films: MutableStateFlow<FilmState> = MutableStateFlow(FilmState.Loading())
    val films: StateFlow<FilmState> = _films.asStateFlow()

    private var _genres = emptyList<String>()
    val genres: List<String> get() = _genres

    private var _selectedGenre = ""
    val selectedGenre get() = _selectedGenre

    private val _filmsOriginal = mutableListOf<Film>()

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
                        setupGenres(data)
                        _films.value = FilmState.Success(data)
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
        _genres = set.toList().sortedBy { it }
    }


    fun filtering(genre: String) {
        try {
            _selectedGenre = genre
            if (genre.isNotEmpty()) {
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