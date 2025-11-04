package com.m4ykey.stos.search.presentation

import com.m4ykey.stos.question.presentation.list.QuestionOrder
import com.m4ykey.stos.question.presentation.list.QuestionSort

sealed interface SearchListAction {
    data class OnQuestionClick(val id : Int) : SearchListAction
    data class OnTagClick(val tag : String) : SearchListAction
    data class OnSearchClick(val inTitle : String, val tag : String = "") : SearchListAction
    data class OnSortClick(val sort : QuestionSort) : SearchListAction
    data class OnOrderClick(val order : QuestionOrder) : SearchListAction
}