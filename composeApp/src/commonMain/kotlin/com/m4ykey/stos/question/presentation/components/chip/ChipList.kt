package com.m4ykey.stos.question.presentation.components.chip

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m4ykey.stos.core.views.CenteredContent
import com.m4ykey.stos.question.presentation.list.QuestionSort
import kmp_stos.composeapp.generated.resources.Res
import kmp_stos.composeapp.generated.resources.activity
import kmp_stos.composeapp.generated.resources.creation
import kmp_stos.composeapp.generated.resources.hot
import kmp_stos.composeapp.generated.resources.month
import kmp_stos.composeapp.generated.resources.votes
import kmp_stos.composeapp.generated.resources.week
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChipList(
    modifier: Modifier = Modifier,
    selectedChip : QuestionSort,
    onChipSelected : (QuestionSort) -> Unit
) {
    val chipList = QuestionSort.entries.map { sort ->
        sort.getLabel() to sort
    }

    CenteredContent(modifier = modifier) { contentModifier ->
        LazyRow(
            modifier = contentModifier.padding(horizontal = 5.dp)
        ) {
            items(chipList) { (label, sortKey) ->
                ChipItem(
                    title = label,
                    selected = selectedChip == sortKey,
                    onSelect = { onChipSelected(sortKey) },
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun QuestionSort.getLabel() : String {
    return when (this)  {
        QuestionSort.HOT -> stringResource(Res.string.hot)
        QuestionSort.ACTIVITY -> stringResource(Res.string.activity)
        QuestionSort.VOTES -> stringResource(Res.string.votes)
        QuestionSort.WEEK -> stringResource(Res.string.week)
        QuestionSort.MONTH -> stringResource(Res.string.month)
        QuestionSort.CREATION -> stringResource(Res.string.creation)
    }
}