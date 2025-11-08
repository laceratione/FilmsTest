package ru.example.kolsatest.presentation.filmlist

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class FilmListItemDecoration(
    private val spanCount: Int,
    private val outSpacing: Int,
    private val spacingBetweenColumn: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        val adapter = parent.adapter
        adapter?.let {
            when (adapter.getItemViewType(position)) {
                MainAdapter.HEADER_TYPE -> {
                    outRect.top =
                        if (position == 0) spacingBetweenColumn else spacingBetweenColumn * 2
                    outRect.bottom = if (position == 0) return else spacingBetweenColumn
                }

                MainAdapter.GENRE_TYPE -> {}

                MainAdapter.FILM_TYPE -> {
                    val column = position % spanCount

                    outRect.left = if (column == 1) outSpacing else spacingBetweenColumn / 2
                    outRect.right = if (column == 0) outSpacing else spacingBetweenColumn / 2
                }
            }
        }
    }
}