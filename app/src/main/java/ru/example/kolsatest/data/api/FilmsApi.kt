package ru.example.kolsatest.data.api

import retrofit2.http.GET
import ru.example.kolsatest.data.model.FilmsDTO

interface FilmsApi {
    @GET("sequeniatesttask/films.json")
    suspend fun getFilms(): FilmsDTO
}