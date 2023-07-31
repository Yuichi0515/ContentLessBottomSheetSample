package com.example.contentlessbottomsheetsample

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BackgroundLessBottomSheet(
    modifier: Modifier = Modifier,
    sheetHeight: Dp = BackgroundLessBottomSheetDefaults.SheetHeight,
    sheetPeekHeight: Dp = BackgroundLessBottomSheetDefaults.SheetPeekHeight,
    initialState: BottomSheetSwipeableState = BottomSheetSwipeableState.COLLAPSED,
    sheetContent: @Composable () -> Unit
) {
    val bottomSheetHeightPx = with(LocalDensity.current) { sheetHeight.toPx() }
    val swipeableState = rememberSwipeableState(initialValue = initialState)
    val peekHeightPx = with(LocalDensity.current) { sheetPeekHeight.toPx() }

    Layout(content = {
        Box(
            modifier = modifier
                .swipeable(
                    state = swipeableState,
                    anchors = mapOf(
                        // y座標が(peekHeightPx - bottomSheetHeightPx)の時、ボトムシートは折り畳まれている状態
                        (peekHeightPx - bottomSheetHeightPx) to BottomSheetSwipeableState.COLLAPSED,
                        // y座標が0fの時、ボトムシートは広げられている状態
                        0f to BottomSheetSwipeableState.EXPANDED
                    ),
                    orientation = Orientation.Vertical,
                    reverseDirection = true
                )
                .nestedScroll(swipeableState.BackgroundLessBottomSheetNestedScrollConnection)
                .fillMaxWidth()
                .height(sheetHeight)
        ) {
            sheetContent()
        }
    }) { measurables, constraints ->
        val boxPlaceable = measurables.first().measure(constraints)
        layout(width = constraints.maxWidth, height = constraints.maxHeight) {
            boxPlaceable.place(
                0,
                constraints.maxHeight - bottomSheetHeightPx.roundToInt() - swipeableState.offset.value.roundToInt()
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private val <T>  SwipeableState<T>.BackgroundLessBottomSheetNestedScrollConnection: NestedScrollConnection
    get() = object : NestedScrollConnection {
        override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            val delta = available.y
            return if (delta < 0 && source == NestedScrollSource.Drag) {
                -performDrag(-delta).toOffset()
            } else {
                Offset.Zero
            }
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            return if (source == NestedScrollSource.Drag) {
                -performDrag(-available.y).toOffset()
            } else {
                Offset.Zero
            }
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            val toFling = available.y
            return if (toFling < 0 && offset.value > Float.NEGATIVE_INFINITY) {
                performFling(velocity = toFling)
                available
            } else {
                Velocity.Zero
            }
        }

        override suspend fun onPostFling(
            consumed: Velocity,
            available: Velocity
        ): Velocity {
            performFling(
                velocity = available.y
            )
            return available
        }

        private fun Float.toOffset(): Offset = Offset(0f, this)
    }

private object BackgroundLessBottomSheetDefaults {
    val SheetHeight = 200.dp
    val SheetPeekHeight = 60.dp
}

@Preview(showBackground = true)
@Composable
@Suppress("MagicNumber")
fun BackgroundLessBottomSheetPreview() {
    BackgroundLessBottomSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            repeat(10) {
                Text(
                    text = "this is text$it", modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}