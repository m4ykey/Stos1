package com.m4ykey.stos.question.presentation.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.stos.question.domain.model.Question
import com.m4ykey.stos.question.domain.usecase.QuestionUseCase
import com.m4ykey.stos.question.presentation.list.QuestionListState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class QuestionTagViewModel(
    private val useCase : QuestionUseCase
) : ViewModel() {

    private val _questionListState = MutableStateFlow(QuestionListState())
    val questionListState = _questionListState.asStateFlow()

    private val tagFlowCache = mutableMapOf<String, Flow<PagingData<Question>>>()

    fun getQuestionsTag(tag : String) : Flow<PagingData<Question>> {
        return tagFlowCache.getOrPut(tag) {
            _questionListState
                .map { it.sort to it.order }
                .distinctUntilChanged()
                .debounce(500L)
                .flatMapLatest { (sort, order) ->
                    useCase.getQuestionsByTag(
                        order = order.name,
                        sort = sort.name,
                        tagged = tag
                    )
                }
                .cachedIn(viewModelScope)
        }
    }

}