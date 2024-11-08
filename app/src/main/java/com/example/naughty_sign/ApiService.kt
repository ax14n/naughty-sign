package com.example.naughty_sign

import retrofit2.Response
import retrofit2.http.GET

/**
 * Interfaz que actua como interfaz para la obtención de los datos del JSON subido a PostMan.
 */
interface ApiService {

    @GET("likes")
    suspend fun getLikes(): Response<List<Like>>

    @GET("matches")
    suspend fun getMatches(): Response<List<Match>>

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

}