package com.example.mypeeloff.bandageanimation

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

import com.example.mypeeloff.bandageanimation.util.toPx


@Composable
fun BandageReveal(
    behindContent: @Composable (revealedFraction: Float) -> Unit = {},
    content: @Composable () -> Unit = {},
    scratchedContent: @Composable () -> Unit = content
) {
    BoxWithConstraints {
        val width = maxWidth
        val maximumScroll = (width.toPx() * 0.8f).toInt()
        var revealedPixels by remember {
            mutableStateOf(0)
        }
        val scrollState = rememberScrollableState {
            revealedPixels = (revealedPixels + it.toInt()).coerceIn(0, maximumScroll)
            it
        }
        val revealedPixelsInDp = with(
            LocalDensity.current
        ) { revealedPixels.toDp() }

        Box(
            Modifier
                .scrollable(scrollState, Orientation.Horizontal, reverseDirection = true),
            contentAlignment = Alignment.Center
        ) {
            val revealedFraction = revealedPixels.toFloat() / width.toPx().toFloat()

            val baseModifier = Modifier.width(width)
            if (revealedPixelsInDp > 0.dp)
                behindContent(revealedFraction)

            BandageOriginalContainer(
                modifier = baseModifier,
                width = width,
                scratchedAmount = revealedPixelsInDp,
                content = content
            )
            if (revealedPixelsInDp > 0.dp) {
                BandageScratchedContent(
                    modifier = baseModifier,
                    width = width,
                    scratchedAmount = revealedPixelsInDp,
                    content = scratchedContent
                )
            }

        }
    }

}

