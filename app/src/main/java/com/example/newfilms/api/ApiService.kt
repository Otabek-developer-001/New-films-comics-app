package com.example.newfilms.api

import com.example.newfilms.api.response.FilmDetails
import com.example.newfilms.api.response.FilmResponseList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Call<FilmDetails>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Call<FilmResponseList>
}