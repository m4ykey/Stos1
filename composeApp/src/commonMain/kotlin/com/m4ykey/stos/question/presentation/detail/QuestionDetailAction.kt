package com.m4ykey.stos.question.presentation.detail

sealed interface QuestionDetailAction {
    data class OnTagClick(val tag : String) : QuestionDetailAction
}