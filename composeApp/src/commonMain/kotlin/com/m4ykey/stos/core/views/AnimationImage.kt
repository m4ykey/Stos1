package com.m4ykey.stos.core.views

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kmp_stos.composeapp.generated.resources.Res

@Composable
fun AnimationImage() {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/black_cat_animation.json").decodeToString()
        )
    }

    Image(
        contentDescription = "Lottie animation",
        painter = rememberLottiePainter(
            composition = composition,
            iterations = Compottie.IterateForever
        )
    )
}