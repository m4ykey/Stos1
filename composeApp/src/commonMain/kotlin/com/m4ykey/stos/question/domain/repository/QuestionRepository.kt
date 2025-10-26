package com.m4ykey.stos.question.domain.repository

import androidx.paging.PagingData
import com.m4ykey.stos.question.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {

    fun getQuestions(page : Int, pageSize : Int, sort : String, order : String) : Flow<PagingData<Question>>

}