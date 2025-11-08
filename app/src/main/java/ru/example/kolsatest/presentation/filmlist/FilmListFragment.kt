package ru.example.kolsatest.presentation.filmlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.example.kolsatest.R
import ru.example.kolsatest.databinding.FragmentFilmListBinding
import ru.example.kolsatest.domain.model.Film
import ru.example.kolsatest.presentation.mapper.FilmListMapperImpl

private const val TAG = "FilmListFragment"

@AndroidEntryPoint
class FilmListFragment : Fragment() {
    private val viewModel: FilmListViewModel by viewModels()
    private var _binding: FragmentFilmListBinding? = null
    private val binding get() = _binding!!

    private val mainAdapter by lazy {
        MainAdapter(
            onGenreClick = { genre ->
                val newSelectedGenre = if (viewModel.selectedGenre == genre) "" else genre
                viewModel.filtering(newSelectedGenre)
            },
            onFilmClick = { film ->
                val action =
                    FilmListFragmentDirections.actionFilmListFragmentToFilmDetailsFragment(film)
                findNavController().navigate(action)
            })
    }

    private fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(requireContext(), 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position == RecyclerView.NO_POSITION) return 1

                    return when (mainAdapter.getItemViewType(position)) {
                        MainAdapter.HEADER_TYPE -> spanCount
                        MainAdapter.GENRE_TYPE -> spanCount
                        MainAdapter.FILM_TYPE -> 1
                        else -> throw IllegalArgumentException(
                            "Unknown view type: ${
                                mainAdapter.getItemViewType(
                                    position
                                )
                            }"
                        )
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilmListBinding.inflate(inflater, container, false)

        setupMainRecyclerView()
        observeUiState()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupMainRecyclerView() {
        binding.rvMain.layoutManager = createLayoutManager()
        binding.rvMain.adapter = mainAdapter
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.films.collect { state ->
                    when (state) {
                        is FilmState.Success -> showFilms(state.films)
                        is FilmState.Error -> showError(state.message)
                        is FilmState.Loading -> showLoading()
                        is FilmState.Empty -> showEmptyData()
                    }
                }
            }
        }
    }

    private fun showFilms(films: List<Film>) {
        binding.progressBar.visibility = View.GONE

        val items = FilmListMapperImpl.map(
            genresTitle = getString(R.string.genres),
            genres = viewModel.genres,
            selectedGenre = viewModel.selectedGenre,
            filmsTitle = getString(R.string.films),
            films = films,
        )

        mainAdapter.submitList(items)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSnackBar() {
        val snackbar =
            Snackbar.make(requireView(), getString(R.string.error_load_msg), Snackbar.LENGTH_SHORT)

        val snackbarText =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackbarText.setTextAppearance(R.style.snackbarText)

        val snackbarAction =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        snackbarAction.setTextAppearance(R.style.snackbarAction)

        snackbar.apply {
            setAction(getString(R.string.repeat)) {}
            setActionTextColor(ContextCompat.getColor(requireContext(), R.color.primary_200))
            show()
        }
    }

    private fun showError(message: String?) {
        binding.progressBar.visibility = View.GONE
        message?.let { Log.e(TAG, "Films list load error: $it") }
        showSnackBar()
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showEmptyData() {
        mainAdapter.submitList(emptyList())
        showToast(getString(R.string.no_results_search))
    }
}