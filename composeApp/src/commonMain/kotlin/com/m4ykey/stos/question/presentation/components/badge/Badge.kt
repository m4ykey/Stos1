package com.m4ykey.stos.question.presentation.components.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m4ykey.stos.question.domain.model.BadgeCounts

@Composable
fun Badge(
    color : Color,
    number : Int
) {
    Row(
        modifier = Modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ColorCircle(color = color)
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            fontSize = 13.sp,
            text = number.toString()
        )
    }
}

@Composable
fun ColorCircle(
    color: Color
) {
    Box(
        modifier = Modifier
            .background(color, shape = CircleShape)
            .size(14.dp)
            .clip(CircleShape)
    )
}

@Composable
fun BadgeRow(
    badgeCounts: BadgeCounts
) {
    val badges = buildList {
        if (badgeCounts.gold > 0) add(Color(0xFFFFCC01) to badgeCounts.gold)
        if (badgeCounts.silver > 0) add(Color(0xFFB4B8BC) to badgeCounts.silver)
        if (badgeCounts.bronze > 0) add(Color(0xFFD1A684) to badgeCounts.bronze)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        badges.forEach { (color, count) ->
            Badge(color = color, number = count)
        }
    }
}