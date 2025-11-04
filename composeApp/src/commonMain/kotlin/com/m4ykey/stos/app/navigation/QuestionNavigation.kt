package com.m4ykey.stos.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.m4ykey.stos.question.presentation.detail.QuestionDetailScreen
import com.m4ykey.stos.question.presentation.list.QuestionListScreen
import com.m4ykey.stos.question.presentation.tag.QuestionTagScreen

fun NavGraphBuilder.questionNavigation(navHostController: NavHostController) {
    composable(Route.QuestionHome.route) {
        QuestionListScreen(
            onQuestionClick = { id ->
                navHostController.navigate(Route.QuestionDetail.route(id)) {
                    launchSingleTop = true
                }
            },
            onSearch = {
                navHostController.navigate(Route.Search.route) {
                    launchSingleTop = true
                }
            }
        )
    }
    composable(
        route = Route.QuestionDetail.routeWithArgs,
        arguments = Route.QuestionDetail.arguments
    ) { navBackStackEntry ->
        val id = navBackStackEntry.savedStateHandle.get<Int>(Route.QuestionDetail.getIdKey()) ?: return@composable
        QuestionDetailScreen(
            onBack = { navHostController.popBackStack() },
            id = id,
            onTagClick = { tag ->
                navHostController.navigate(Route.QuestionTag.route(tag))
            }
        )
    }
    composable(
        route = Route.QuestionTag.routeWithArgs,
        arguments = Route.QuestionTag.arguments
    ) { navBackStackEntry ->
        val tag = navBackStackEntry.savedStateHandle.get<String>(Route.QuestionTag.getIdTag()) ?: return@composable
        QuestionTagScreen(
            tag = tag,
            onBack = { navHostController.popBackStack() },
            onQuestionClick = { id ->
                navHostController.navigate(Route.QuestionDetail.route(id)) {
                    launchSingleTop = true
                }
            }
        )
    }
}