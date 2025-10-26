package com.m4ykey.stos.question.domain.model

data class QuestionDetail(
    val tags : List<String>,
    val owner : QuestionOwner,
    val viewCount : Int,
    val downVoteCount : Int,
    val upVoteCount : Int,
    val answerCount : Int,
    val lastActivityDate : Int,
    val creationDate : Int,
    val questionId : Int,
    val bodyMarkdown : String,
    val link : String,
    val title : String
)
