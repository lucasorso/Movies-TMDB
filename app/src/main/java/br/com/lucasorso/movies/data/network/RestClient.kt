package br.com.lucasorso.movies.data.network

import br.com.lucasorso.movies.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClient {

    companion object {
        const val BASE_URL: String = "https://api.themoviedb.org/"
        const val IMAGE_URL = "http://image.tmdb.org/t/p/"
        const val API_KEY: String = BuildConfig.API_KEY
        const val LANGUAGE: String = "pt-BR"
        const val REGION: String = ""
    }

    fun getRetrofit(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
    }
}

