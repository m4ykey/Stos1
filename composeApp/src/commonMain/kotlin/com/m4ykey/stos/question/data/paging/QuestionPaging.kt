package com.m4ykey.stos.question.data.paging

import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.core.network.safeApi
import com.m4ykey.stos.core.paging.BasePagingSource
import com.m4ykey.stos.question.data.mapper.toDomain
import com.m4ykey.stos.question.data.network.RemoteQuestionService
import com.m4ykey.stos.question.domain.model.Question

class QuestionPaging(
    private val service : RemoteQuestionService,
    private val sort : String,
    private val order : String
) : BasePagingSource<Question>() {

    override suspend fun loadData(
        page: Int,
        pageSize: Int
    ): Result<List<Question>> {
        return safeApi {
            service.getQuestions(page, pageSize, sort = sort, order = order)
        }.run {
            when (this) {
                is ApiResult.Failure -> Result.failure(exception)
                is ApiResult.Success -> {
                    val questions = data.items.map { it.toDomain() }
                    Result.success(questions)
                }
            }
        }
    }
}