package com.m4ykey.stos.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
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

    }
}