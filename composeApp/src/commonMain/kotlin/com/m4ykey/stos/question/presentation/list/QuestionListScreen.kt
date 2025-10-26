package com.m4ykey.stos.question.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.m4ykey.stos.core.views.BasePagingList
import com.m4ykey.stos.question.domain.model.Question
import com.m4ykey.stos.question.presentation.components.ChipList
import com.m4ykey.stos.question.presentation.components.QuestionItem
import kmp_stos.composeapp.generated.resources.Res
import kmp_stos.composeapp.generated.resources.search
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionListScreen(
    viewModel: QuestionListViewModel = koinViewModel(),
    onQuestionClick : (Int) -> Unit
) {
    val questions = viewModel.getQuestionsFlow().collectAsLazyPagingItems()
    val viewState by viewModel.questionListState.collectAsStateWithLifecycle()
    val sort = viewState.sort

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val lazyListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    val onAction = viewModel::onAction

    LaunchedEffect(Unit) {
        viewModel.listUiEvent.collectLatest { event ->
            when (event) {
                is ListUiEvent.ChangeSort -> viewModel.updateSort(event.sort)
                is ListUiEvent.OnQuestionClick -> onQuestionClick(event.id)
                else -> null
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {},
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            contentDescription = stringResource(Res.string.search),
                            imageVector = Icons.Default.Search
                        )
                    }
                }
            )
        }
    ) { padding ->
        QuestionListContent(
            padding = padding,
            listState = lazyListState,
            questions = questions,
            sort = sort,
            onAction = onAction
        )
    }
}

@Composable
fun QuestionListContent(
    listState : LazyListState,
    sort : QuestionSort,
    padding : PaddingValues,
    questions : LazyPagingItems<Question>,
    onAction : (QuestionListAction) -> Unit
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
                        onAction(QuestionListAction.OnQuestionClick(question.questionId))
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