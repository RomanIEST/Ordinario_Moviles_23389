package com.example.examen_ordinario.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class JokeResponse(
    val id: String,
    val value: String,
    val icon_url: String
)

interface ChuckNorrisService {
    @GET("jokes/random")
    suspend fun getRandomJoke(): JokeResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://api.chucknorris.io/"

    val service: ChuckNorrisService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChuckNorrisService::class.java)
    }
}
