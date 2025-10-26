package com.m4ykey.stos.question.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDetailDto(
    val tags : List<String>? = emptyList(),
    val owner : QuestionOwnerDto? = null,
    @SerialName("view_count") val viewCount : Int? = 0,
    @SerialName("down_vote_count") val downVoteCount : Int? = 0,
    @SerialName("up_vote_count") val upVoteCount : Int? = 0,
    @SerialName("answer_count") val answerCount : Int? = 0,
    @SerialName("last_activity_date") val lastActivityDate : Int? = 0,
    @SerialName("creation_date") val creationDate : Int? = 0,
    @SerialName("question_id") val questionId : Int? = 0,
    @SerialName("body_markdown") val bodyMarkdown : String? = null,
    val link : String? = null,
    val title : String? = null
)