package com.bvn.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.bvn.newsapp.presentation.news_screen.NewsScreen
import com.bvn.newsapp.presentation.news_screen.NewsScreenViewModel
import com.bvn.newsapp.presentation.theme.NewsAppTheme
import com.bvn.newsapp.util.NavGraphSetup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                val navController = rememberNavController()
                NavGraphSetup(navController = navController)

            }
        }
    }
}