package com.m4ykey.stos.question.data.network

import com.m4ykey.stos.core.Filters.QUESTION_ANSWER_FILTER
import com.m4ykey.stos.core.Filters.QUESTION_DETAIL_FILTER
import com.m4ykey.stos.core.Filters.QUESTION_FILTER
import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.question.data.network.model.QuestionAnswerDto
import com.m4ykey.stos.question.data.network.model.QuestionDetailDto
import com.m4ykey.stos.question.data.network.model.QuestionDto

interface RemoteQuestionService {

    suspend fun getQuestionsAnswers(
        filter : String = QUESTION_ANSWER_FILTER,
        id : Int
    ) : Items<QuestionAnswerDto>

    suspend fun getQuestions(
        page : Int,
        pageSize : Int,
        filter : String = QUESTION_FILTER,
        sort : String,
        order : String
    ) : Items<QuestionDto>

    suspend fun getQuestionById(
        filter : String = QUESTION_DETAIL_FILTER,
        id : Int
    ) : Items<QuestionDetailDto>

    suspend fun getQuestionsByTag(
        filter: String = QUESTION_FILTER,
        page : Int,
        pageSize : Int,
        sort : String,
        order : String,
        tagged : String
    ) : Items<QuestionDto>

}