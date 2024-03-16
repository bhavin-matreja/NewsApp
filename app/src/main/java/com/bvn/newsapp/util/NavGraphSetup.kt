package com.bvn.newsapp.util

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bvn.newsapp.presentation.article_screen.ArticleScreen
import com.bvn.newsapp.presentation.news_screen.NewsScreen
import com.bvn.newsapp.presentation.news_screen.NewsScreenViewModel

@Composable
fun NavGraphSetup(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "news_screen") {
        composable(route = "news_screen") {
            val viewModel: NewsScreenViewModel = hiltViewModel()
            NewsScreen(
                state = viewModel.state,
                onEvent = viewModel::onEvent,
                onReadFullStoryButtonClicked = { url ->
                    navController.navigate("article_screen?web_url=$url")
                }
            )
        }
        composable(
            route = "article_screen?web_url={web_url}",
            arguments = listOf(navArgument(name = "web_url") {
                type = NavType.StringType
            })
        ) {
            ArticleScreen(
                url = it.arguments?.getString("web_url"),
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
    }
}