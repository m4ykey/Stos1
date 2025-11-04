package com.m4ykey.stos.question.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.compose.LazyPagingItems
import com.m4ykey.stos.question.domain.model.Question
import com.m4ykey.stos.question.presentation.list.QuestionListAction
import com.m4ykey.stos.question.presentation.list.QuestionListContent
import com.m4ykey.stos.question.presentation.list.QuestionListState
import kmp_stos.composeapp.generated.resources.Res
import kmp_stos.composeapp.generated.resources.back
import kmp_stos.composeapp.generated.resources.order
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseQuestionListScreen(
    title : String? = null,
    onBack : (() -> Unit)? = null,
    questions : LazyPagingItems<Question>,
    viewState : QuestionListState,
    onAction : (QuestionListAction) -> Unit,
    onQuestionClick: (Int) -> Unit,
    actions : @Composable RowScope.() -> Unit = {}
) {
    val sort = viewState.sort
    val order = viewState.order

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val lazyListState = rememberLazyListState()

    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val showScrollToTopButton by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 30 || lazyListState.firstVisibleItemScrollOffset > 0
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = { if (title != null) Text(text = title) },
                navigationIcon = {
                    onBack?.let {
                        IconButton(onClick = it) {
                            Icon(
                                contentDescription = stringResource(Res.string.back),
                                imageVector = Icons.AutoMirrored.Default.ArrowBack
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            contentDescription = stringResource(Res.string.order),
                            imageVector = Icons.Default.Reorder
                        )
                    }
                    actions()
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTopButton,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it }
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    },
                    content = {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Default.ArrowUpward
                        )
                    }
                )
            }
        }
    ) { padding ->
        QuestionListContent(
            padding = padding,
            listState = lazyListState,
            questions = questions,
            sort = sort,
            onAction = onAction,
            onQuestionClick = onQuestionClick
        )
    }

    if (showDialog) {
        OrderDialog(
            order = order,
            onSelectOrder = { selected ->
                onAction(QuestionListAction.OnOrderClick(selected))
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}