package com.example.contentlessbottomsheetsample

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun NestedScrollableBottomSheetByLayout() {
    val bottomSheetHeight = 200.dp
    val bottomSheetHeightPx = with(LocalDensity.current) { bottomSheetHeight.toPx() }
    val swipeableState = rememberSwipeableState(initialValue = BottomSheetSwipeableState.COLLAPSED)
    val peekHeightPx = with(LocalDensity.current) { 60.dp.toPx() }

    Layout(content = {
        Column(modifier = Modifier
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
            .height(bottomSheetHeight)
            .verticalScroll(rememberScrollState())
//            .nestedScroll(object : NestedScrollConnection {
//                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//                    return Offset.Zero
//                }
//            })
        ) {
            repeat(10) {
                Text(text = "this is text$it", modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth())
            }
        }
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
    NestedScrollableBottomSheetByLayout()
}