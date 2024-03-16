package com.bvn.newsapp.presentation.news_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bvn.newsapp.domain.model.Article
import com.bvn.newsapp.domain.repository.NewsRepository
import com.bvn.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsScreenViewModel @Inject constructor(
    val newsRepository: NewsRepository
) : ViewModel() {

    var state by mutableStateOf(NewsScreenState())

    private var searchJob: Job? = null

    // This is not required anymore
/*    init {
        getNewsArticles(category = "general")
    }*/

    fun onEvent(event: NewsScreenEvent) {
        when (event) {
            is NewsScreenEvent.onCategoryChanged -> {
                state = state.copy(category = event.category)
                getNewsArticles(state.category)
            }

            NewsScreenEvent.onCloseIconClicked -> {
                state = state.copy(isSearchBarVisible = false)
                getNewsArticles(category = state.category)
            }

            is NewsScreenEvent.onNewsCardClicked -> {
                state = state.copy(selectedArticle = event.article)
            }

            NewsScreenEvent.onSearchIconClicked -> {
                state = state.copy(isSearchBarVisible = true, articles = emptyList())
            }

            is NewsScreenEvent.onSearchQueryChanged -> {
                state = state.copy(searchQuery = event.searchQuery)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(1000)
                    searchForNews(state.searchQuery)
                }
            }
        }
    }

    private fun getNewsArticles(category: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = newsRepository.getTopHeadlines(category)
            when (result) {
                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        articles = emptyList()
                    )
                }

                is Resource.Success -> {
                    state = state.copy(
                        articles = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
//                    articles = result.data ?: emptyList()
                }
            }
        }
    }

    private fun searchForNews(query: String) {
        if (query.isEmpty()) return
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = newsRepository.searchForNews(query)
            when (result) {
                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        articles = emptyList()
                    )
                }

                is Resource.Success -> {
                    state = state.copy(
                        articles = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
//                    articles = result.data ?: emptyList()
                }
            }
        }
    }
}