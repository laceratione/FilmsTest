package ru.example.kolsatest.domain.model

import java.io.Serializable

data class Film(
    val id: Int,
    val localizedName: String,
    val name: String,
    val year: Int,
    val rating: Float?,
    val imageUrl: String?,
    val description: String?,
    val genres: List<String>,
) : Serializable