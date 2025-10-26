package com.m4ykey.stos.question.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m4ykey.stos.core.views.TextMarkdown
import com.m4ykey.stos.owner.presentation.components.OwnerCard
import com.m4ykey.stos.question.domain.model.Question
import kmp_stos.composeapp.generated.resources.Res
import kmp_stos.composeapp.generated.resources.answer_count
import kmp_stos.composeapp.generated.resources.arrow_down
import kmp_stos.composeapp.generated.resources.arrow_up
import kmp_stos.composeapp.generated.resources.views
import org.jetbrains.compose.resources.painterResource

@Composable
fun QuestionItem(
    modifier: Modifier = Modifier,
    question: Question,
    onQuestionClick : (Int) -> Unit
) {
    val formattedReputation = formatReputation(question.owner.reputation)
    val formattedDate = formatCreationDate(question.creationDate.toLong())

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { onQuestionClick(question.questionId) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OwnerCard(
                modifier = Modifier
                    .clickable {}
                    .align(Alignment.CenterVertically),
                owner = question.owner
            )
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = modifier.fillMaxWidth()) {
                TextMarkdown(
                    text = question.owner.displayName,
                    fontSize = 14.sp
                )
                Text(
                    fontSize = 13.sp,
                    text = formattedReputation
                )
            }
        }
        TextMarkdown(
            text = question.title
        )
        QuestionStatsRow(
            item = question,
            textSize = 14.sp,
            iconSize = 15.dp
        )
        Text(
            text = formattedDate,
            fontSize = 12.sp
        )
    }
}

@Composable
fun QuestionStatsRow(
    item : Question,
    textSize : TextUnit,
    iconSize : Dp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        QuestionCount(
            count = getVoteCount(item.downVoteCount > 1, item).toInt(),
            textSize = textSize,
            iconSize = iconSize,
            iconRes = getArrowPosition(item.downVoteCount > 1)
        )
        QuestionCount(
            count = item.answerCount,
            textSize = textSize,
            iconSize = iconSize,
            iconRes = getAnswerCountPainter()
        )
        QuestionCount(
            count = item.viewCount,
            textSize = textSize,
            iconSize = iconSize,
            iconRes = getViewsCountPainter()
        )
    }
}

private fun getVoteCount(isDownVote : Boolean, question: Question) : String {
    return if (isDownVote) {
        "-${question.downVoteCount}"
    } else {
        "${question.upVoteCount}"
    }
}

@Composable
private fun getViewsCountPainter() : Painter {
    return painterResource(resource = Res.drawable.views)
}

@Composable
private fun getAnswerCountPainter() : Painter {
    return painterResource(resource = Res.drawable.answer_count)
}

@Composable
private fun getArrowPosition(isDownVote: Boolean) : Painter {
    return if (isDownVote) {
        painterResource(resource = Res.drawable.arrow_down)
    } else {
        painterResource(resource = Res.drawable.arrow_up)
    }
}