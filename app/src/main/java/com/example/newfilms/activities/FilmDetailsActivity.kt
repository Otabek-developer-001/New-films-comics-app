package com.example.newfilms.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import coil.load
import coil.size.Scale
import com.example.newfilms.R
import com.example.newfilms.api.ApiClient
import com.example.newfilms.api.ApiService
import com.example.newfilms.databinding.ActivityFilmDetailsBinding
import com.example.newfilms.api.response.FilmDetails
import com.example.newfilms.utils.Constants.POSTER_BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmDetailsBinding
    private val api: ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val movieId = intent.getIntExtra("id", 1)
        binding.apply {
            prgBarMovies.visibility = View.VISIBLE
            val callMoviesApi = api.getMovieDetails(movieId)
            callMoviesApi.enqueue(object : Callback<FilmDetails> {
                override fun onResponse(call: Call<FilmDetails>, response: Response<FilmDetails>) {
                    Log.e("onFailure", "Err : ${response.code()}")
                    prgBarMovies.visibility = View.GONE
                    when (response.code()) {
                        in 200..299 -> {
                            response.body()?.let { itBody ->
                                val moviePosterURL = POSTER_BASE_URL + itBody.posterPath
                                imgMovie.load(moviePosterURL) {
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }
                                imgMovieBack.load(moviePosterURL) {
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }
                                tvMovieTitle.text = itBody.title
                                tvMovieTagLine.text = itBody.tagline
                                tvMovieDateRelease.text = itBody.releaseDate
                                tvMovieRating.text = itBody.voteAverage.toString()
                                tvMovieRuntime.text = itBody.runtime.toString()
                                tvMovieBudget.text = itBody.budget.toString()
                                tvMovieRevenue.text = itBody.revenue.toString()
                                tvMovieOverview.text = itBody.overview
                            }
                        }
                        in 300..399 -> {
                            Log.d("Response Code", " Redirection messages : ${response.code()}")
                        }
                        in 400..499 -> {
                            Log.d("Response Code", " Client error responses : ${response.code()}")
                        }
                        in 500..599 -> {
                            Log.d("Response Code", " Server error responses : ${response.code()}")
                        }
                    }
                }
                override fun onFailure(call: Call<FilmDetails>, t: Throwable) {
                    prgBarMovies.visibility = View.GONE
                    Log.e("onFailure", "Err : ${t.message}")
                }
            })
        }
    }
}