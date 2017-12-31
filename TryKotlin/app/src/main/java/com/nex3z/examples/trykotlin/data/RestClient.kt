package com.nex3z.examples.trykotlin.data

import com.nex3z.examples.trykotlin.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RestClient {

    private val mMovieService: MovieService

    init {
        var httpClient: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor { chain ->
                    val original: Request = chain.request()
                    val originalHttpUrl: HttpUrl = original.url()
                    val builder: HttpUrl.Builder = originalHttpUrl.newBuilder()
                            .addQueryParameter("api_key", BuildConfig.API_KEY)
                    val requestBuilder: Request.Builder = original.newBuilder()
                            .url(builder.build())
                            .method(original.method(), original.body())
                    val request: Request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org")
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        mMovieService = retrofit.create(MovieService::class.java)
    }

    fun getMovieService(): MovieService {
        return mMovieService
    }
}
