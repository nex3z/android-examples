package com.nex3z.examples.trykotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.nex3z.examples.trykotlin.data.MovieService
import com.nex3z.examples.trykotlin.data.RestClient
import com.nex3z.examples.trykotlin.data.entity.DiscoveryMovieEntity
import com.nex3z.examples.trykotlin.data.entity.MovieEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val restClient: RestClient = RestClient()
    private val adapter: MovieAdapter = MovieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_movie.adapter = adapter;
        rv_movie.layoutManager = GridLayoutManager(this, 2)

        val movieService: MovieService = restClient.getMovieService()
        val call = movieService.discoverMovies(1, "popularity.desc")

        call.enqueue(object: Callback<DiscoveryMovieEntity> {
            override fun onResponse(call: Call<DiscoveryMovieEntity>?, response: Response<DiscoveryMovieEntity>) {
                if (response.isSuccessful()) {
                    val movieResponse: DiscoveryMovieEntity? = response.body()
                    val movies: List<MovieEntity>? = movieResponse?.results
                    Log.v("MainActivity", "onResponse(): mMovies size = " + movies?.size)

                    if (movies != null) {
                        adapter.movies = movies
                    }
                } else {
                    val statusCode = response.code()
                    Toast.makeText(this@MainActivity,
                            "Error code: " + statusCode, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DiscoveryMovieEntity>?, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }
}
