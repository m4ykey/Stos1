package com.m4ykey.stos.question.domain.usecase

import androidx.paging.PagingData
import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.question.domain.model.Question
import com.m4ykey.stos.question.domain.model.QuestionAnswer
import com.m4ykey.stos.question.domain.model.QuestionDetail
import com.m4ykey.stos.question.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow

class QuestionUseCase(
    private val repository: QuestionRepository
) {

    fun getQuestionsAnswer(
        id : Int
    ) : Flow<ApiResult<List<QuestionAnswer>>> {
        return repository.getQuestionsAnswer(id = id)
    }

    fun getQuestionById(
        id : Int
    ) : Flow<ApiResult<QuestionDetail>> {
        return repository.getQuestionById(id = id)
    }

    fun getQuestions(
        page : Int = 1,
        pageSize : Int = 20,
        sort : String,
        order : String
    ) : Flow<PagingData<Question>> {
        return repository.getQuestions(page, pageSize, sort, order)
    }

}