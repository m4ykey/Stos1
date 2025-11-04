package com.m4ykey.stos.question.presentation.list

interface ListUiEvent {
    data class ChangeSort(val sort: QuestionSort) : ListUiEvent
    data class OnQuestionClick(val id: Int) : ListUiEvent
    data class ChangeOrder(val order : QuestionOrder) : ListUiEvent
    data class TagClick(val tag : String) : ListUiEvent
    data class NavigateToSearch(val inTitle : String, val tag : String = "") : ListUiEvent
}