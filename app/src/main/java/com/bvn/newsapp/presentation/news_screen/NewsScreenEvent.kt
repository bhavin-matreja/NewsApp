package com.bvn.newsapp.presentation.news_screen

import com.bvn.newsapp.domain.model.Article

sealed class NewsScreenEvent{
    data class onNewsCardClicked(val article: Article): NewsScreenEvent()
    data class onCategoryChanged(val category: String): NewsScreenEvent()
    data class onSearchQueryChanged(val searchQuery: String): NewsScreenEvent()
    object onSearchIconClicked : NewsScreenEvent()
    object onCloseIconClicked : NewsScreenEvent()
}
