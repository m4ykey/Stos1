package com.m4ykey.stos.question.presentation.detail

import com.m4ykey.stos.question.domain.model.QuestionDetail

data class QuestionDetailState(
    val errorMessage : String? = null,
    val question : QuestionDetail? = null,
    val loading : Boolean = false
)
