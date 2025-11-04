package com.m4ykey.stos.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.m4ykey.stos.search.presentation.SearchListScreen
import com.m4ykey.stos.search.presentation.SearchScreen

fun NavGraphBuilder.searchNavigation(navHostController: NavHostController) {
    composable(route = Route.Search.route) {
        SearchScreen(
            onBack = { navHostController.navigateUp() },
            onSearchScreen = { inTitle, tag ->
                navHostController.navigate(Route.SearchList.route(inTitle, tag)) {
                    launchSingleTop = true
                }
            }
        )
    }

    composable(
        route = Route.SearchList.routeWithArgs,
        arguments = Route.SearchList.arguments
    ) { navBackStackEntry ->
        val tag = navBackStackEntry.savedStateHandle.get<String>(Route.SearchList.getIdTag()) ?: return@composable
        val inTitle = navBackStackEntry.savedStateHandle.get<String>(Route.SearchList.getIdInTitle()) ?: return@composable

        SearchListScreen(
            onBack = { navHostController.navigateUp() },
            tag = tag,
            inTitle = inTitle,
            onQuestionClick = { questionId ->
                navHostController.navigate(Route.QuestionDetail.route(questionId)) {
                    launchSingleTop = true
                }
            }
        )
    }
}