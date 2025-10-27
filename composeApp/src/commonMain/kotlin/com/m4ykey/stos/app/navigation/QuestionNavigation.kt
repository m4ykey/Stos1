package com.m4ykey.stos.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.m4ykey.stos.question.presentation.detail.QuestionDetailScreen
import com.m4ykey.stos.question.presentation.list.QuestionListScreen

fun NavGraphBuilder.questionNavigation(navHostController: NavHostController) {
    composable(Route.QuestionHome.route) {
        QuestionListScreen(
            onQuestionClick = { id ->
                navHostController.navigate(Route.QuestionDetail.route(id)) {
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
            onNavBack = {
                navHostController.popBackStack()
            },
            id = id
        )
    }
}