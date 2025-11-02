package com.m4ykey.stos.question.presentation.tag

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import com.m4ykey.stos.question.presentation.components.BaseQuestionListScreen
import com.m4ykey.stos.question.presentation.list.ListUiEvent
import com.m4ykey.stos.question.presentation.list.QuestionListViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionTagScreen(
    tag : String,
    onBack : (() -> Unit),
    viewModel: QuestionTagViewModel = koinViewModel(),
    listViewModel : QuestionListViewModel = koinViewModel(),
    onQuestionClick : (Int) -> Unit
) {
    val questions = viewModel.getQuestionsTag(tag).collectAsLazyPagingItems()
    val viewState by viewModel.questionListState.collectAsState()
    val onAction = listViewModel::onAction

    LaunchedEffect(Unit) {
        listViewModel.listUiEvent.collectLatest { event ->
            when (event) {
                is ListUiEvent.ChangeSort -> listViewModel.updateSort(event.sort)
                is ListUiEvent.OnQuestionClick -> onQuestionClick(event.id)
                is ListUiEvent.ChangeOrder -> listViewModel.updateOrder(event.order)
            }
        }
    }

    BaseQuestionListScreen(
        title = tag,
        questions = questions,
        onQuestionClick = onQuestionClick,
        onBack = onBack,
        viewState = viewState,
        onAction = onAction
    )
}