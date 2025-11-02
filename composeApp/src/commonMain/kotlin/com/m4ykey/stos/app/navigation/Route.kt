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

    object QuestionTag {
        private const val ID_TAG = "tagged"
        const val base = "question_tag"

        const val routeWithArgs = "$base/{$ID_TAG}"

        val arguments = listOf(
            navArgument(ID_TAG) { type = NavType.StringType }
        )

        fun route(tag : String) = "$base/$tag"
        fun getIdTag() : String = ID_TAG
    }

}