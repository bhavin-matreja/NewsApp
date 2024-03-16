package com.bvn.newsapp.presentation.news_screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bvn.newsapp.domain.model.Article
import com.bvn.newsapp.presentation.component.BottomSheetContent
import com.bvn.newsapp.presentation.component.CategoryTabRow
import com.bvn.newsapp.presentation.component.NewsArticleCard
import com.bvn.newsapp.presentation.component.NewsScreenTopBar
import com.bvn.newsapp.presentation.component.RetryContent
import com.bvn.newsapp.presentation.component.SearchAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun NewsScreen(
    state: NewsScreenState,
    onEvent: (NewsScreenEvent) -> Unit,
    onReadFullStoryButtonClicked: (String) -> Unit
) {
//    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val categories = listOf(
        "General", "Business", "Health", "Science", "Sports", "Technology", "Entertainment"
    )
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { categories.size }
    )

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var shouldBottomSheetShow by remember {
        mutableStateOf(false)
    }

    //Keyboard should open whenever we click on search icon
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    if (shouldBottomSheetShow) {
        ModalBottomSheet(
            onDismissRequest = { shouldBottomSheetShow = false },
            sheetState = sheetState
        ) {
            state.selectedArticle?.let {
                BottomSheetContent(
                    article = it,
                    onReadFullStoryButtonClicked = {
                        shouldBottomSheetShow = false
                        onReadFullStoryButtonClicked(it.url)
                    }
                )
            }
        }
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect() { page ->
            onEvent(NewsScreenEvent.onCategoryChanged(category = categories[page]))
        }
    }
    
    LaunchedEffect(key1 = Unit) {
        if (state.searchQuery.isNotEmpty()) {
            onEvent(NewsScreenEvent.onSearchQueryChanged(state.searchQuery))
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Crossfade(targetState = state.isSearchBarVisible) { isVisible ->
            if (isVisible) {
                Column {
                    SearchAppBar(
                        modifier = Modifier.focusRequester(focusRequester),
                        value = state.searchQuery,
                        onValueChange = {
                            onEvent(NewsScreenEvent.onSearchQueryChanged(it))
                        },
                        onCloseIconClicked = { onEvent(NewsScreenEvent.onCloseIconClicked) },
                        onSearchClicked = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                    NewsArticleList(
                        state = state,
                        onNewsCardClicked = {
                            shouldBottomSheetShow = true
                            onEvent(NewsScreenEvent.onNewsCardClicked(it))
                        },
                        onRetry = {
                            onEvent(NewsScreenEvent.onCategoryChanged(state.category))
                        }
                    )
                }
            } else {
                Scaffold(
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        NewsScreenTopBar(
//                scrollBehavior = scrollBehavior,
                            onSearchIconClicked = {
                                coroutineScope.launch {
                                    delay(500)
                                    focusRequester.requestFocus()
                                }
                                onEvent(NewsScreenEvent.onSearchIconClicked)
                            }
                        )
                    }
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(padding)
                    ) {
                        CategoryTabRow(
                            pagerState = pagerState,
                            categories = categories,
                            onTabSelected = { index ->
                                coroutineScope.launch { pagerState.animateScrollToPage(index) }
                            }
                        )
                        HorizontalPager(
                            state = pagerState,
                        ) {
                            NewsArticleList(
                                state = state,
                                onNewsCardClicked = {
                                    shouldBottomSheetShow = true
                                    onEvent(NewsScreenEvent.onNewsCardClicked(it))
                                },
                                onRetry = {
                                    onEvent(NewsScreenEvent.onCategoryChanged(state.category))
                                }
                            )
                        }
                    }
                }
            }
        }
    }


}

@Composable
fun NewsArticleList(
    state: NewsScreenState,
    onNewsCardClicked: (Article) -> Unit,
    onRetry: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(state.articles) { article ->
            NewsArticleCard(article = article, onCardClicked = onNewsCardClicked)
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (state.isLoading) {
            CircularProgressIndicator()
        }
        if (state.error != null) {
            RetryContent(error = state.error, onRetry = onRetry)
        }
    }
}