package com.m4ykey.stos.question.presentation.detail

import com.m4ykey.stos.question.domain.model.QuestionAnswer

data class QuestionAnswerState(
    val errorMessage : String? = null,
    val answer : List<QuestionAnswer> = emptyList(),
    val loading : Boolean = false
)
