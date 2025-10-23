package ru.example.kolsatest.data.model

import com.google.gson.annotations.SerializedName

data class FilmsDTO(
    @SerializedName("films") val films: List<FilmDTO>,
)

fun FilmsDTO.mapToDomain() = films.map { it.mapToDomain() }