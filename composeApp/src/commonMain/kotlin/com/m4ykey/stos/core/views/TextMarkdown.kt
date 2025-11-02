package com.m4ykey.stos.core.views

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.model.ImageData
import com.mikepenz.markdown.model.ImageTransformer
import com.mikepenz.markdown.model.MarkdownTypography
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes

val coil3ImageTransfer = object : ImageTransformer {
    @Composable
    override fun transform(link: String): ImageData? {
        val cleanUrl = remember(link) {
            link.trim().let { url ->
                when {
                    url.isBlank() -> null
                    url.startsWith("//") -> "https:$url"
                    url.startsWith("/") -> null
                    url.startsWith("http://") && !url.startsWith("https://") -> "https://$url"
                    else -> url
                }
            }
        } ?: return null

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(cleanUrl)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
        )

        var imageState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

        return when (imageState) {
            is AsyncImagePainter.State.Error -> null
            is AsyncImagePainter.State.Success -> {
                ImageData(
                    contentDescription = "Image from $cleanUrl",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    painter = painter
                )
            }
            is AsyncImagePainter.State.Loading -> {
                ImageData(
                    contentDescription = "Loading image",
                    painter = painter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
            }
            else -> {
                ImageData(
                    painter = painter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentDescription = "Image from $cleanUrl"
                )
            }
        }
    }
}

@Composable
fun TextMarkdown(
    text : String,
    modifier : Modifier = Modifier,
    fontSize : TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal
) {
    val isDarkTheme = isSystemInDarkTheme()

    val markdownContent = remember(text) {
        text.decodeHtml().trimIndent()
    }

    val highlightBuilder = remember(isDarkTheme) {
        Highlights.Builder().theme(SyntaxThemes.atom(darkMode = isDarkTheme))
    }

    val customComponents = markdownComponents(
        codeBlock = {
            MarkdownHighlightedCodeBlock(
                content = it.content,
                node = it.node,
                highlightsBuilder = highlightBuilder
            )
        },
        codeFence = {
            MarkdownHighlightedCodeFence(
                content = it.content,
                node = it.node,
                highlightsBuilder = highlightBuilder
            )
        }
    )

    val customTypography = remember(fontSize, fontWeight) {
        object : MarkdownTypography {
            override val h1 = TextStyle(fontSize = fontSize * 1.6f, fontWeight = fontWeight)
            override val h2 = TextStyle(fontSize = fontSize * 1.4f, fontWeight = fontWeight)
            override val h3 = TextStyle(fontSize = fontSize * 1.2f, fontWeight = fontWeight)
            override val h4 = TextStyle(fontSize = fontSize * 1.1f, fontWeight = fontWeight)
            override val h5 = TextStyle(fontSize = fontSize, fontWeight = fontWeight)
            override val h6 = TextStyle(fontSize = fontSize * 0.9f, fontWeight = fontWeight)
            override val paragraph = TextStyle(fontSize = fontSize, fontWeight = fontWeight)
            override val text = paragraph
            override val bullet = paragraph
            override val list = paragraph
            override val ordered = paragraph
            override val quote = TextStyle(fontSize = fontSize, fontStyle = FontStyle.Italic)
            override val code = TextStyle(fontSize = fontSize * 0.9f, fontFamily = FontFamily.Monospace)
            override val inlineCode = code
            override val table = paragraph
            override val textLink = TextLinkStyles(
                style = SpanStyle(
                    color = Color(0xFF1E88E5),
                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Markdown(
            modifier = modifier.fillMaxWidth(),
            imageTransformer = coil3ImageTransfer,
            typography = customTypography,
            components = customComponents,
            content = markdownContent
        )
    }
}