package com.example.contentlessbottomsheetsample

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheetByLayout() {
    val bottomSheetHeight = 200.dp
    val bottomSheetHeightPx = with(LocalDensity.current) { bottomSheetHeight.toPx() }
    val swipeableState = rememberSwipeableState(initialValue = BottomSheetSwipeableState.COLLAPSED)
    val peekHeightPx = with(LocalDensity.current) { 60.dp.toPx() }

    Layout(content = {
        Box(modifier = Modifier
            .background(Color.Red)
            .swipeable(
                state = swipeableState,
                anchors = mapOf(
                    (peekHeightPx - bottomSheetHeightPx) to BottomSheetSwipeableState.COLLAPSED,
                    0f to BottomSheetSwipeableState.EXPANDED
                ),
                orientation = Orientation.Vertical,
                reverseDirection = true
            )
            .fillMaxWidth()
            .height(bottomSheetHeight))
    }) { measurables, constraints ->
        val boxPlaceable = measurables.first().measure(constraints)
        layout(width = constraints.maxWidth, height = constraints.maxHeight) {
            boxPlaceable.place(0, constraints.maxHeight - bottomSheetHeightPx.roundToInt() - swipeableState.offset.value.roundToInt())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetByLayoutPreview() {
    BottomSheetByLayout()
}