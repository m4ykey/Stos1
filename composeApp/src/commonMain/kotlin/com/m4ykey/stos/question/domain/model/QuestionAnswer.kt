package com.m4ykey.stos.question.domain.model

data class QuestionAnswer(
    val bodyMarkdown : String,
    val creationDate : Int,
    val owner : QuestionOwner,
    val downVoteCount : Int,
    val answerId : Int,
    val upVoteCount : Int
)
