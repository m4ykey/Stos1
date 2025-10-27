package com.m4ykey.stos.question.presentation.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import kmp_stos.composeapp.generated.resources.ascending
import kmp_stos.composeapp.generated.resources.descending
import kmp_stos.composeapp.generated.resources.order
import kmp_stos.composeapp.generated.resources.search
import kmp_stos.composeapp.generated.resources.select_order
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
    val order = viewState.order

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val lazyListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    val onAction = viewModel::onAction

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.listUiEvent.collectLatest { event ->
            when (event) {
                is ListUiEvent.ChangeSort -> viewModel.updateSort(event.sort)
                is ListUiEvent.OnQuestionClick -> onQuestionClick(event.id)
                is ListUiEvent.ChangeOrder -> viewModel.updateOrder(event.order)
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
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            contentDescription = stringResource(Res.string.order),
                            imageVector = Icons.Default.Reorder
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

    if (showDialog) {
        AnimatedVisibility(
            visible = showDialog,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            BasicAlertDialog(
                onDismissRequest = { showDialog = false }
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 6.dp,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = stringResource(resource = Res.string.select_order),
                            modifier = Modifier.padding(bottom = 16.dp),
                            style = MaterialTheme.typography.titleLarge
                        )

                        val descOrder = QuestionOrder.DESC
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAction(QuestionListAction.OnOrderClick(descOrder))
                                    showDialog = false
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = order == descOrder,
                                onClick = {
                                    onAction(QuestionListAction.OnOrderClick(descOrder))
                                    showDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(resource = Res.string.descending))
                        }

                        val ascOrder = QuestionOrder.ASC
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAction(QuestionListAction.OnOrderClick(ascOrder))
                                    showDialog = false
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = order == ascOrder,
                                onClick = {
                                    onAction(QuestionListAction.OnOrderClick(ascOrder))
                                    showDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(resource = Res.string.ascending))
                        }
                    }
                }
            }
        }
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