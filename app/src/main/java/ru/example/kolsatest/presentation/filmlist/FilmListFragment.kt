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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.example.kolsatest.R
import ru.example.kolsatest.databinding.FragmentFilmListBinding
import ru.example.kolsatest.domain.model.Film

private const val TAG = "FilmListFragment"

@AndroidEntryPoint
class FilmListFragment : Fragment() {
    private val viewModel: FilmListViewModel by viewModels()
    private var binding: FragmentFilmListBinding? = null
    private var filmAdapter: FilmAdapter? = null
    private var genreAdapter: GenreAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilmListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupScrollChangedListener()
        setupGenreRecyclerView()
        setupFilmRecyclerView()
        observeGenres()
        observeFilms()
    }

    private fun setupScrollChangedListener(){
        view?.post {
            binding?.nestedScrollView?.scrollTo(0, viewModel.scrollPosition)
        }

        binding?.nestedScrollView?.viewTreeObserver?.addOnScrollChangedListener {
            viewModel.scrollPosition = binding?.nestedScrollView?.scrollY ?: 0
        }
    }

    private fun setupGenreRecyclerView(){
        genreAdapter = GenreAdapter(onItemClick = { genre, pos ->
            viewModel.filtering(genre, pos)
        },
            initSelectedPosition = viewModel.selectedPosition
        )
        binding?.rvGenre?.adapter = genreAdapter
    }

    private fun setupFilmRecyclerView() {
        filmAdapter = FilmAdapter(onItemClick = { film ->
            val action =
                FilmListFragmentDirections.actionFilmListFragmentToFilmDetailsFragment(film)
            findNavController().navigate(action)
        })
        binding?.rvFilm?.adapter = filmAdapter
        binding?.rvFilm?.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun observeGenres(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.genres.collect { listGenres ->
                    showGenres(listGenres)
                }
            }
        }
    }

    private fun showGenres(genres: List<String>){
        genreAdapter?.updateGenres(genres)
    }

    private fun observeFilms() {
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
        binding?.progressBar?.visibility = View.GONE
        binding?.tvFilms?.visibility = View.VISIBLE
        binding?.tvGenres?.visibility = View.VISIBLE
        filmAdapter?.submitList(films)

    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSnackBar() {
        val snackbar = Snackbar.make(requireView(), getString(R.string.error_load_msg), Snackbar.LENGTH_SHORT)

        val snackbarText = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackbarText.setTextAppearance(R.style.snackbarText)

        val snackbarAction = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        snackbarAction.setTextAppearance(R.style.snackbarAction)

        snackbar.apply {
            setAction(getString(R.string.repeat)) {}
            setActionTextColor(ContextCompat.getColor(requireContext(), R.color.primary_200))
            show()
        }
    }

    private fun showError(message: String?) {
        binding?.progressBar?.visibility = View.GONE
        message?.let { Log.e(TAG, "Films list load error: $it") }
        showSnackBar()
    }

    private fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun showEmptyData() {
        filmAdapter?.submitList(emptyList())
        showToast(getString(R.string.no_results_search))
    }
}