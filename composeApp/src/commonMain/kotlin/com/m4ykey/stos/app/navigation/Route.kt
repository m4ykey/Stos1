package com.m4ykey.stos.app.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Route(val route : String) {

    object QuestionHome : Route(route = "question_home")

    object QuestionDetail {
        const val base = "question_detail"
        const val routeWithArgs = "$base/{id}"
        val arguments = listOf(navArgument("id") { type = NavType.IntType })
        fun route(id : Int) = "$base/$id"
    }

}