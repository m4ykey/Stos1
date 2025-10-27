package com.m4ykey.stos.app.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Route(val route : String) {

    object QuestionHome : Route(route = "question_home")

    object QuestionDetail {
        private const val ID_KEY = "id"
        const val base = "question_detail"

        const val routeWithArgs = "$base/{$ID_KEY}"

        val arguments = listOf(
            navArgument(ID_KEY) { type = NavType.IntType }
        )

        fun route(id : Int) = "$base/$id"

        fun getIdKey() : String = ID_KEY
    }

}