package com.bvn.newsapp.data.repository

import com.bvn.newsapp.data.remote.NewsApi
import com.bvn.newsapp.domain.model.Article
import com.bvn.newsapp.domain.repository.NewsRepository
import com.bvn.newsapp.util.Resource
import kotlinx.coroutines.delay

class NewsRepositoryImpl(
    private val newsApi: NewsApi
): NewsRepository {

    override suspend fun getTopHeadlines(category: String): Resource<List<Article>> {
        return try {
            val response = newsApi.getBreakingNews(category = category)
            delay(1000)
            Resource.Success(response.articles)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message)
        }
    }

    override suspend fun searchForNews(query: String): Resource<List<Article>> {
        return try {
            val response = newsApi.searchForNews(query = query)
            delay(1000)
            Resource.Success(response.articles)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message)
        }
    }
}