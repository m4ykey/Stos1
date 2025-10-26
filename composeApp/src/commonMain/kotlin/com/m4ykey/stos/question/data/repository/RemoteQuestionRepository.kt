package com.m4ykey.stos.question.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.stos.core.paging.pagingConfig
import com.m4ykey.stos.question.data.network.RemoteQuestionService
import com.m4ykey.stos.question.data.paging.QuestionPaging
import com.m4ykey.stos.question.domain.model.Question
import com.m4ykey.stos.question.domain.repository.QuestionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class RemoteQuestionRepository(
    private val remoteQuestionService: RemoteQuestionService,
    private val dispatcherIO : CoroutineDispatcher
) : QuestionRepository {

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