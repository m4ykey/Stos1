package com.m4ykey.stos.question.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.m4ykey.stos.core.views.BasePagingList
import com.m4ykey.stos.question.domain.model.Question
import com.m4ykey.stos.question.presentation.components.BaseQuestionListScreen
import com.m4ykey.stos.question.presentation.components.chip.ChipList
import com.m4ykey.stos.question.presentation.components.QuestionItem
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionListScreen(
    viewModel: QuestionListViewModel = koinViewModel(),
    onQuestionClick : (Int) -> Unit
) {
    val questions = viewModel.getQuestionsFlow().collectAsLazyPagingItems()
    val viewState by viewModel.questionListState.collectAsState()
    val onAction = viewModel::onAction

    LaunchedEffect(Unit) {
        viewModel.listUiEvent.collectLatest { event ->
            when (event) {
                is ListUiEvent.ChangeSort -> viewModel.updateSort(event.sort)
                is ListUiEvent.OnQuestionClick -> onQuestionClick(event.id)
                is ListUiEvent.ChangeOrder -> viewModel.updateOrder(event.order)
            }
        }
    }

    BaseQuestionListScreen(
        questions = questions,
        viewState = viewState,
        onAction = onAction,
        onQuestionClick = onQuestionClick
    )
}

@Composable
fun QuestionListContent(
    listState : LazyListState,
    sort : QuestionSort,
    padding : PaddingValues,
    questions : LazyPagingItems<Question>,
    onAction : (QuestionListAction) -> Unit,
    onQuestionClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        ChipList(
            selectedChip = sort,
            onChipSelected = { selectedSort ->
                onAction(QuestionListAction.OnSortClick(selectedSort))
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasePagingList(
            itemContent = { question ->
                QuestionItem(
                    question = question,
                    onQuestionClick = {
                        onQuestionClick(question.questionId)
                    }
                )
            },
            items = questions,
            modifier = Modifier.fillMaxWidth(),
            listState = listState,
            itemKey = { it.questionId }
        )
    }
}