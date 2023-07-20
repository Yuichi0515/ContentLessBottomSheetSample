package com.example.contentlessbottomsheetsample

import android.util.Log
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
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NestedScrollableBottomSheetByLayout() {
    val bottomSheetHeight = 200.dp
    val bottomSheetHeightPx = with(LocalDensity.current) { bottomSheetHeight.toPx() }
    val swipeableState = rememberSwipeableState(initialValue = BottomSheetSwipeableState.COLLAPSED)
    val peekHeightPx = with(LocalDensity.current) { 60.dp.toPx() }
    val scrollState = rememberScrollState()

    Log.d("bottomsheet", "offset = ${swipeableState.offset.value.roundToInt()}")

    Layout(content = {
        Box(
            modifier = Modifier
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
                .nestedScroll(object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        val delta = available.toFloat()
                        return if (delta < 0 && source == NestedScrollSource.Drag) {
                            -swipeableState
                                .performDrag(-delta)
                                .toOffset()
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
                            -swipeableState
                                .performDrag(-available.toFloat())
                                .toOffset()
                        } else {
                            Offset.Zero
                        }
                    }

                    override suspend fun onPreFling(available: Velocity): Velocity {
                        val toFling = Offset(available.x, available.y).toFloat()
                        return if (toFling < 0 && swipeableState.offset.value > Float.NEGATIVE_INFINITY) {
                            swipeableState.performFling(velocity = toFling)
                            available
                        } else {
                            Velocity.Zero
                        }
                    }

                    override suspend fun onPostFling(
                        consumed: Velocity,
                        available: Velocity
                    ): Velocity {
                        swipeableState.performFling(
                            velocity = Offset(
                                available.x,
                                available.y
                            ).toFloat()
                        )
                        return available
                    }

                    private fun Float.toOffset(): Offset = Offset(0f, this)

                    private fun Offset.toFloat(): Float = this.y
                })
                .fillMaxWidth()
                .height(bottomSheetHeight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .verticalScroll(scrollState)
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

@Preview(showBackground = true)
@Composable
private fun BottomSheetByLayoutPreview() {
    NestedScrollableBottomSheetByLayout()
}