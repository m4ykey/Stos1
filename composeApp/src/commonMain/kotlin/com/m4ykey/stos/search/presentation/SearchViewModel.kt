package com.m4ykey.stos.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.stos.question.presentation.list.ListUiEvent
import com.m4ykey.stos.question.presentation.list.QuestionListState
import com.m4ykey.stos.question.presentation.list.QuestionOrder
import com.m4ykey.stos.question.presentation.list.QuestionSort
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow(SearchQueryState())

    private val _questionListState = MutableStateFlow(QuestionListState())
    val questionListState = _questionListState.asStateFlow()

    private val _listUiEvent = MutableSharedFlow<ListUiEvent>()
    val listUiEvent = _listUiEvent.asSharedFlow()

    fun onAction(action: SearchListAction) {
        viewModelScope.launch {
            val event = when (action) {
                is SearchListAction.OnQuestionClick -> ListUiEvent.OnQuestionClick(action.id)
                is SearchListAction.OnTagClick -> ListUiEvent.TagClick(action.tag)
                is SearchListAction.OnSearchClick -> ListUiEvent.NavigateToSearch(action.inTitle, action.tag)
                is SearchListAction.OnSortClick -> ListUiEvent.ChangeSort(action.sort)
                is SearchListAction.OnOrderClick -> ListUiEvent.ChangeOrder(action.order)
            }
            _listUiEvent.emit(event)
        }
    }

    fun updateSort(sort: QuestionSort) {
        _questionListState.update { it.copy(sort = sort) }
    }

    fun updateOrder(order : QuestionOrder) {
        _questionListState.update { it.copy(order = order) }
    }

    fun searchQuestion(inTitle : String, tag : String) {
        _searchQuery.value = SearchQueryState(inTitle = inTitle, tag = tag)
    }
}