package com.m4ykey.stos.question.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m4ykey.stos.core.views.TextMarkdown
import com.m4ykey.stos.question.domain.model.QuestionAnswer
import com.m4ykey.stos.question.domain.model.QuestionOwner
import com.m4ykey.stos.question.presentation.detail.DisplayOwner

@Composable
fun AnswerItem(
    answer : QuestionAnswer,
    owner : QuestionOwner
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        DisplayOwner(item = owner)
        Spacer(modifier = Modifier.height(5.dp))
        TextMarkdown(text = answer.bodyMarkdown)
    }
}