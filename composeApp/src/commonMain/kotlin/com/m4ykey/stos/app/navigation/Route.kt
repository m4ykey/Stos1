package com.m4ykey.stos.app.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Route(val route : String) {

    object QuestionHome : Route(route = "question_home")
    object Search : Route(route = "search")

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

    object SearchList {
        private const val ID_TAG = "tag"
        private const val ID_IN_TITLE = "inTitle"
        const val base = "search_list"

        const val routeWithArgs = "$base/{inTitle}/{tag}"

        val arguments = listOf(
            navArgument("inTitle") { type = NavType.StringType },
            navArgument("tag") { type = NavType.StringType }
        )

        fun route(inTitle : String, tag : String) = "$base/$inTitle/$tag"
        fun getIdTag() : String = ID_TAG
        fun getIdInTitle() : String = ID_IN_TITLE
    }

}