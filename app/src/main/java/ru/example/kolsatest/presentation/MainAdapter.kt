package ru.example.kolsatest.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.example.kolsatest.databinding.ListItemFilmBinding
import ru.example.kolsatest.databinding.ListItemGenreBinding
import ru.example.kolsatest.databinding.ListItemHeaderBinding
import ru.example.kolsatest.domain.model.Film
import ru.example.kolsatest.presentation.filmlist.FilmDiffUtil
import ru.example.kolsatest.presentation.filmlist.FilmHolder
import ru.example.kolsatest.presentation.filmlist.GenreDiffUtil
import ru.example.kolsatest.presentation.filmlist.GenreHolder
import ru.example.kolsatest.presentation.filmlist.HeaderDiffUtil
import ru.example.kolsatest.presentation.filmlist.HeaderHolder

//import ru.example.kolsatest.presentation.filmlist.FilmAdapter
//import ru.example.kolsatest.presentation.filmlist.GenreAdapter

class MainAdapter(
    private val onGenreClick: (String) -> Unit,
    private val onFilmClick: (Film) -> Unit,
) : ListAdapter<MainListItem, RecyclerView.ViewHolder>(MainDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MainListItem.HeaderItem -> HEADER_TYPE
            is MainListItem.GenreItem -> GENRE_TYPE
            is MainListItem.FilmItem -> FILM_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            HEADER_TYPE -> {
                val binding = ListItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderHolder(binding)
            }

            GENRE_TYPE -> {
                val binding = ListItemGenreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GenreHolder(binding, onGenreClick)
            }

            FILM_TYPE -> {
                val binding = ListItemFilmBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FilmHolder(binding, onFilmClick)
            }

            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item: MainListItem = getItem(position)) {
            is MainListItem.HeaderItem -> (holder as HeaderHolder).bind(item.text)
            is MainListItem.GenreItem -> (holder as GenreHolder).bind(
                genre = item.name,
                isSelected = item.isSelected
            )

            is MainListItem.FilmItem -> (holder as FilmHolder).bind(item.film)
        }
    }

    class MainDiffUtil(
        private val headerDiffUtil: HeaderDiffUtil = HeaderDiffUtil(),
        private val genreDiffUtil: GenreDiffUtil = GenreDiffUtil(),
        private val filmDiffUtil: FilmDiffUtil = FilmDiffUtil(),
    ) : DiffUtil.ItemCallback<MainListItem>() {
        override fun areItemsTheSame(oldItem: MainListItem, newItem: MainListItem): Boolean {
            return when {
                oldItem is MainListItem.HeaderItem && newItem is MainListItem.HeaderItem ->
                    headerDiffUtil.areItemsTheSame(oldItem, newItem)

                oldItem is MainListItem.GenreItem && newItem is MainListItem.GenreItem ->
                    genreDiffUtil.areItemsTheSame(oldItem, newItem)

                oldItem is MainListItem.FilmItem && newItem is MainListItem.FilmItem ->
                    filmDiffUtil.areItemsTheSame(oldItem, newItem)

                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: MainListItem, newItem: MainListItem): Boolean {
            return when {
                oldItem is MainListItem.HeaderItem && newItem is MainListItem.HeaderItem ->
                    headerDiffUtil.areContentsTheSame(oldItem, newItem)

                oldItem is MainListItem.GenreItem && newItem is MainListItem.GenreItem ->
                    genreDiffUtil.areContentsTheSame(oldItem, newItem)

                oldItem is MainListItem.FilmItem && newItem is MainListItem.FilmItem ->
                    filmDiffUtil.areContentsTheSame(oldItem, newItem)

                else -> false
            }
        }

    }

    companion object {
        const val HEADER_TYPE = 0
        const val GENRE_TYPE = 1
        const val FILM_TYPE = 2
    }
}