package com.example.contentlessbottomsheetsample

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableBottomSheet() {
    val swipeableState = rememberSwipeableState(
        initialValue = BottomSheetSwipeableState.COLLAPSED
    )

    val peekHeightPx = with(LocalDensity.current) { 60.dp.toPx() }
    val sheetHeight = 200.dp
    val sheetHeightPx = with(LocalDensity.current) { sheetHeight.toPx() }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
            .offset { IntOffset(0, -swipeableState.offset.value.roundToInt()) }
    ) {
        Box(
            modifier = Modifier
                .background(Color.Black)
                .swipeable(
                    state = swipeableState,
                    anchors = mapOf(
                        (peekHeightPx - sheetHeightPx) to BottomSheetSwipeableState.COLLAPSED,
                        0f to BottomSheetSwipeableState.EXPANDED
                    ),
                    orientation = Orientation.Vertical,
                    reverseDirection = true
                )
                .fillMaxWidth()
                .wrapContentHeight()
            , contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Red)
                    .height(sheetHeight)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SwipeableBottomSheetPreview() {
    SwipeableBottomSheet()
}