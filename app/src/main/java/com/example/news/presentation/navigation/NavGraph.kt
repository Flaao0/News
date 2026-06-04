package com.example.news.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.news.presentation.screen.articledetails.ArticleDetailsScreen
import com.example.news.presentation.screen.favorites.FavoritesScreen
import com.example.news.presentation.screen.settings.SettingsScreen
import com.example.news.presentation.screen.subscriptions.SubscriptionsScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Subscriptions.route
    ) {
        composable(Screen.Subscriptions.route) {
            SubscriptionsScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToFavorites = {
                    navController.navigate(Screen.Favorites.route)
                },
                onArticleClick = { articleUrl ->
                    navController.navigate(Screen.ArticleDetails.createRoute(articleUrl))
                },
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onArticleClick = { articleUrl ->
                    navController.navigate(Screen.ArticleDetails.createRoute(articleUrl))
                },
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.ArticleDetails.route,
            arguments = listOf(
                navArgument("articleUrl") { type = NavType.StringType },
            ),
        ) {
            ArticleDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}

sealed class Screen(val route: String) {

    data object Subscriptions : Screen("subscriptions")

    data object Settings : Screen("settings")

    data object Favorites : Screen("favorites")

    data object ArticleDetails : Screen("article_details/{articleUrl}") {

        fun createRoute(articleUrl: String): String {
            return "article_details/${Uri.encode(articleUrl)}"
        }
    }
}
