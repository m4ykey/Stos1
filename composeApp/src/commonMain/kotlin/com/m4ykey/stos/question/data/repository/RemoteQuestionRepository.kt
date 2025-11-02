package com.m4ykey.stos.question.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.core.network.safeApi
import com.m4ykey.stos.core.paging.pagingConfig
import com.m4ykey.stos.question.data.mapper.toDomain
import com.m4ykey.stos.question.data.network.RemoteQuestionService
import com.m4ykey.stos.question.data.paging.QuestionPaging
import com.m4ykey.stos.question.data.paging.QuestionTagPaging
import com.m4ykey.stos.question.domain.model.Question
import com.m4ykey.stos.question.domain.model.QuestionAnswer
import com.m4ykey.stos.question.domain.model.QuestionDetail
import com.m4ykey.stos.question.domain.repository.QuestionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteQuestionRepository(
    private val remoteQuestionService: RemoteQuestionService,
    private val dispatcherIO : CoroutineDispatcher
) : QuestionRepository {

    override fun getQuestionsByTag(
        page: Int,
        pageSize: Int,
        sort: String,
        order: String,
        tagged: String
    ): Flow<PagingData<Question>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                QuestionTagPaging(order = order, sort = sort, service = remoteQuestionService, tagged = tagged)
            }
        ).flow.flowOn(dispatcherIO)
    }

    override fun getQuestionsAnswer(id: Int): Flow<ApiResult<List<QuestionAnswer>>> {
        return flow {
            val result = safeApi { remoteQuestionService.getQuestionsAnswers(id = id) }

            when (result) {
                is ApiResult.Failure -> emit(ApiResult.Failure(result.exception))
                is ApiResult.Success -> {
                    val answers = result.data.items.map { it.toDomain() }
                    emit(ApiResult.Success(answers))
                }
            }
        }.flowOn(dispatcherIO)
    }

    override fun getQuestionById(id: Int): Flow<ApiResult<QuestionDetail>> {
        return flow {
            val result = safeApi { remoteQuestionService.getQuestionById(id = id) }

            when (result) {
                is ApiResult.Failure -> emit(ApiResult.Failure(result.exception))
                is ApiResult.Success -> {
                    val questions = result.data.items.map { it.toDomain() }.firstOrNull()
                    if (questions != null) emit(ApiResult.Success(questions))
                }
            }
        }.flowOn(dispatcherIO)
    }

    override fun getQuestions(
        page: Int,
        pageSize: Int,
        sort: String,
        order : String
    ): Flow<PagingData<Question>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                QuestionPaging(service = remoteQuestionService, sort = sort, order = order)
            }
        ).flow.flowOn(dispatcherIO)
    }
}