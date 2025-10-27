package com.m4ykey.stos.question.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionAnswerDto(
    @SerialName("body_markdown") val bodyMarkdown : String? = null,
    @SerialName("creation_date") val creationDate : Int? = 0,
    val owner : QuestionOwnerDto? = null,
    @SerialName("down_vote_count") val downVoteCount : Int? = 0,
    @SerialName("answer_id") val answerId : Int? = 0,
    @SerialName("up_vote_count") val upVoteCount : Int? = 0
)
