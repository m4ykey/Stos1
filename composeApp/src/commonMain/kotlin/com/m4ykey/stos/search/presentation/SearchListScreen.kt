package com.m4ykey.stos.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.m4ykey.stos.question.presentation.list.ListUiEvent
import kmp_stos.composeapp.generated.resources.Res
import kmp_stos.composeapp.generated.resources.back
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchListScreen(
    tag : String,
    inTitle : String,
    onBack : (() -> Unit),
    viewModel: SearchViewModel = koinViewModel(),
    onQuestionClick : (Int) -> Unit
) {

    val initialSearchText = buildString {
        if (inTitle.isNotEmpty()) {
            append(inTitle)
        }
        if (tag.isNotEmpty()) {
            if (inTitle.isNotEmpty()) append(" ")
            append(tag)
        }
    }

    var searchText by rememberSaveable { mutableStateOf(initialSearchText) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

    LaunchedEffect(inTitle, tag) {
        if (initialSearchText.isNotEmpty()) {
            viewModel.searchQuestion(inTitle, tag)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.listUiEvent.collectLatest { event ->
            when (event) {
                is ListUiEvent.OnQuestionClick -> onQuestionClick(event.id)
                is ListUiEvent.ChangeSort -> viewModel.updateSort(event.sort)
                is ListUiEvent.ChangeOrder -> viewModel.updateOrder(event.order)
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {  },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            contentDescription = stringResource(Res.string.back),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SearchBox(
                value = searchText,
                onSearch = {

                },
                onValueChange = { searchText = it },
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}