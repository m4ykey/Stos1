package com.m4ykey.stos.question.data.network

import com.m4ykey.stos.core.Filters.QUESTION_FILTER
import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.question.data.network.model.QuestionDto

interface RemoteQuestionService {

    suspend fun getQuestions(
        page : Int,
        pageSize : Int,
        filter : String = QUESTION_FILTER,
        sort : String,
        order : String
    ) : Items<QuestionDto>

}