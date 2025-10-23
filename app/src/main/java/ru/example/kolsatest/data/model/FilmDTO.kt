package ru.example.kolsatest.data.model

import com.google.gson.annotations.SerializedName
import ru.example.kolsatest.domain.model.Film

data class FilmDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("localized_name") val localizedName: String,
    @SerializedName("name") val name: String,
    @SerializedName("year") val year: Int,
    @SerializedName("rating") val rating: Float?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("genres") val genres: List<String>,
)

fun FilmDTO.mapToDomain() = Film(
    id = this.id,
    localizedName = this.localizedName,
    name=this.name,
    year = this.year,
    rating = this.rating,
    imageUrl = this.imageUrl,
    description = this.description,
    genres = this.genres,
)