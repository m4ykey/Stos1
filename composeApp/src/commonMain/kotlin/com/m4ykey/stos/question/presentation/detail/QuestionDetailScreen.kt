package com.m4ykey.stos.question.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.stos.core.views.TextMarkdown
import com.m4ykey.stos.owner.presentation.components.OwnerCard
import com.m4ykey.stos.question.data.mapper.toQuestion
import com.m4ykey.stos.question.domain.model.QuestionAnswer
import com.m4ykey.stos.question.domain.model.QuestionDetail
import com.m4ykey.stos.question.domain.model.QuestionOwner
import com.m4ykey.stos.question.presentation.components.AnswerItem
import com.m4ykey.stos.question.presentation.components.ChipItem
import com.m4ykey.stos.question.presentation.components.QuestionStatsRow
import com.m4ykey.stos.question.presentation.components.badge.BadgeRow
import com.m4ykey.stos.question.presentation.components.formatCreationDate
import com.m4ykey.stos.question.presentation.components.formatReputation
import kmp_stos.composeapp.generated.resources.Res
import kmp_stos.composeapp.generated.resources.answers
import kmp_stos.composeapp.generated.resources.asked
import kmp_stos.composeapp.generated.resources.back
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionDetailScreen(
    id : Int,
    viewModel : QuestionDetailViewModel = koinViewModel(),
    onNavBack : () -> Unit
) {
    val detailState by viewModel.questionDetailState.collectAsStateWithLifecycle()
    val answerState by viewModel.questionAnswerState.collectAsStateWithLifecycle()

    val loading = detailState.loading || answerState.loading
    val error = detailState.errorMessage ?: answerState.errorMessage

    val detail = detailState.question
    val answers = answerState.answer

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

    LaunchedEffect(id) {
        viewModel.loadQuestions(id)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavBack) {
                        Icon(
                            contentDescription = stringResource(resource = Res.string.back),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                error != null -> {

                }
                detail != null -> {
                    QuestionDetailContent(
                        paddingValues = padding,
                        item = detail,
                        listState = listState,
                        answers = answers
                    )
                }
                else -> {
                    Text(
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.Center),
                        text = "No data"
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionDetailContent(
    modifier : Modifier = Modifier,
    item : QuestionDetail,
    paddingValues: PaddingValues,
    listState : LazyListState,
    answers : List<QuestionAnswer>
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .padding(paddingValues)
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            TextMarkdown(
                text = item.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            TextMarkdown(
                text = item.bodyMarkdown
            )
        }
        item {
            TagListWrap(tags = item.tags)
        }
        item {
            QuestionStatsRow(
                iconSize = 16.dp,
                textSize = 15.sp,
                item = item.toQuestion()
            )
        }
        item {
            Text(
                fontSize = 14.sp,
                text = "${stringResource(resource = Res.string.asked)} ${formatCreationDate(item.creationDate.toLong())}"
            )
        }
        item {
            DisplayOwner(
                item = item.owner
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Text(
                text = "${stringResource(resource = Res.string.answers)}: ${item.answerCount}",
                fontSize = 14.sp
            )
        }
        items(
            items = answers,
            key = { it.answerId },
            contentType = { "answer_item" }
        ) { answer ->
            AnswerItem(
                answer = answer,
                owner = answer.owner
            )
            Spacer(modifier = Modifier.height(5.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 5.dp))
        }
    }
}

@Composable
fun DisplayOwner(
    modifier : Modifier = Modifier,
    item: QuestionOwner
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        OwnerCard(
            owner = item,
            modifier = modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = modifier.width(5.dp))
        Column(modifier = modifier.weight(1f)) {
            TextMarkdown(text = item.displayName)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    fontSize = 13.sp,
                    text = formatReputation(item.reputation)
                )
                BadgeRow(item.badgeCounts)
            }
        }
    }
}

@Composable
fun TagListWrap(
    tags : List<String>
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { label ->
            ChipItem(
                title = label,
                modifier = Modifier,
                selected = false,
                onSelect = {}
            )
        }
    }
}