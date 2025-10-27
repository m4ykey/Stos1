package com.m4ykey.stos.question.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.stos.question.domain.model.Question
import com.m4ykey.stos.question.domain.usecase.QuestionUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class QuestionListViewModel(
    private val useCase: QuestionUseCase
) : ViewModel() {

    private val _questionListState = MutableStateFlow(QuestionListState())
    val questionListState = _questionListState.asStateFlow()

    private val _listUiEvent = MutableSharedFlow<ListUiEvent>()
    val listUiEvent = _listUiEvent.asSharedFlow()

    private val _questionFlow = _questionListState
        .map { it.sort to it.order }
        .distinctUntilChanged()
        .debounce(1000L)
        .flatMapLatest { (sort, order) ->
            useCase.getQuestions(
                sort = sort.name,
                order = order.name
            )
        }
        .cachedIn(viewModelScope)

    fun getQuestionsFlow() : Flow<PagingData<Question>> = _questionFlow

    fun onAction(action: QuestionListAction) {
        viewModelScope.launch {
            val event = when (action) {
                is QuestionListAction.OnSortClick -> ListUiEvent.ChangeSort(action.sort)
                is QuestionListAction.OnOrderClick -> ListUiEvent.ChangeOrder(action.order)
                is QuestionListAction.OnQuestionClick -> ListUiEvent.OnQuestionClick(action.id)
            }
            _listUiEvent.emit(event)
        }
    }

    fun updateSort(sort : QuestionSort) {
        _questionListState.update { it.copy(sort = sort) }
    }

    fun updateOrder(order : QuestionOrder) {
        _questionListState.update { it.copy(order = order) }
    }

}