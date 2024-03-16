package com.bvn.newsapp.domain.repository

import com.bvn.newsapp.domain.model.Article
import com.bvn.newsapp.util.Resource

interface NewsRepository {
    suspend fun getTopHeadlines(category: String): Resource<List<Article>>
    suspend fun searchForNews(query: String): Resource<List<Article>>
}