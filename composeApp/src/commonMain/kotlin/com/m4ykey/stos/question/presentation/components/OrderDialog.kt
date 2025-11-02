package com.m4ykey.stos.question.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m4ykey.stos.question.presentation.list.QuestionOrder
import kmp_stos.composeapp.generated.resources.Res
import kmp_stos.composeapp.generated.resources.ascending
import kmp_stos.composeapp.generated.resources.descending
import kmp_stos.composeapp.generated.resources.select_order
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDialog(
    order : QuestionOrder,
    onDismiss : () -> Unit,
    onSelectOrder : (QuestionOrder) -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        BasicAlertDialog(onDismissRequest = onDismiss) {
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
                    listOf(
                        QuestionOrder.DESC to stringResource(Res.string.descending),
                        QuestionOrder.ASC to stringResource(Res.string.ascending)
                    ).forEach { (value, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectOrder(value) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = order == value,
                                onClick = { onSelectOrder(value) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            }
        }
    }
}