package com.bvn.newsapp.data.remote

import com.bvn.newsapp.domain.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    companion object {
        const val API_KEY = "da38cf98c5bd418f8aa80700691c0c15"
        const val BASE_URL = "https://newsapi.org/v2/"
    }

    @GET("top-headlines")
    suspend fun getBreakingNews(
        @Query("category") category: String,
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

    @GET("everything")
    suspend fun searchForNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

}