package com.example.newfilms.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newfilms.adapter.FilmsAdapter
import com.example.newfilms.api.ApiClient
import com.example.newfilms.api.ApiService
import com.example.newfilms.databinding.ActivityMainBinding
import com.example.newfilms.api.response.FilmResponseList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val filmsAdapter by lazy { FilmsAdapter() }
    private val api: ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            progressBar.visibility = View.VISIBLE
            val callMoviesApi = api.getPopularMovie(1)
            callMoviesApi.enqueue(object : Callback<FilmResponseList> {
                override fun onResponse(call: Call<FilmResponseList>, response: Response<FilmResponseList>) {
                    progressBar.visibility = View.GONE
                    when (response.code()) {
                        in 200..299 -> {
                            Log.d("Response Code", " success messages : ${response.code()}")
                            response.body()?.let { itBody ->
                                itBody.results.let { itData ->
                                    if (itData.isNotEmpty()) {
                                        filmsAdapter.differ.submitList(itData)
                                        recycleTv.apply {
                                            layoutManager = LinearLayoutManager(this@MainActivity)
                                            adapter = filmsAdapter
                                        }
                                    }
                                }
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

                override fun onFailure(call: Call<FilmResponseList>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Log.e("onFailure", "Err : ${t.message}")
                }
            })
        }
    }
}