package com.plcoding.cryptotracker.graph.component

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.detail.domain.model.CoinPrice
import com.plcoding.cryptotracker.graph.model.ChartStyle
import com.plcoding.cryptotracker.graph.model.DataPoint
import com.plcoding.cryptotracker.graph.model.PriceLabel
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun LineChart(
    dataPoints: List<DataPoint>,
    currency: Currency,
    style: ChartStyle,
    visibleDataPointIndices: IntRange,
    modifier: Modifier = Modifier,
    selectedDataPoint: DataPoint? = null,
    onSelectedDataPointChange: (DataPoint) -> Unit = {},
    onXLabelWidthChange: (Float) -> Unit = {},
    showHelperLines: Boolean,
) {
    val textStyle =
        LocalTextStyle.current.copy(
            fontSize = style.labelFontSize,
        )
    val visibleDataPoints: List<DataPoint> =
        remember(dataPoints, visibleDataPointIndices) {
            dataPoints.slice(visibleDataPointIndices)
        }
    val maxYValue =
        remember(visibleDataPoints) {
            visibleDataPoints.maxOfOrNull { it.y } ?: 0f
        }
    val minYValue =
        remember(visibleDataPoints) {
            visibleDataPoints.minOfOrNull { it.y } ?: 0f
        }
    val measurer = rememberTextMeasurer()
    var xLabelWidth by remember {
        mutableFloatStateOf(0f)
    }
    LaunchedEffect(key1 = xLabelWidth) {
        onXLabelWidthChange(xLabelWidth)
    }
    val selectedDataPointIndex =
        remember(selectedDataPoint) {
            dataPoints.indexOf(selectedDataPoint)
        }
    var drawPoints by remember {
        mutableStateOf(dataPoints)
    }
    var isShowingDataPoints by remember {
        mutableStateOf(selectedDataPoint != null)
    }

    Canvas(
        modifier =
            modifier
                .fillMaxSize()
                .pointerInput(drawPoints, xLabelWidth) {
                    detectHorizontalDragGestures { change, _ ->
                        val newSelectedDataPointIndex =
                            getSelectedDataPointIndex(
                                touchOffsetX = change.position.x,
                                triggerWidth = xLabelWidth,
                                drawPoints = drawPoints,
                            )
                        isShowingDataPoints =
                            (newSelectedDataPointIndex + visibleDataPointIndices.first) in visibleDataPointIndices

                        if (isShowingDataPoints) {
                            onSelectedDataPointChange(dataPoints[newSelectedDataPointIndex])
                        }
                    }
                },
    ) {
        val minLabelSpacingYPx = style.minYLabelSpacing.toPx()
        val verticalPaddingPx = style.verticalPadding.toPx()
        val horizontalPaddingPx = style.horizontalPadding.toPx()
        val xLabelSpacingPx = style.xLabelSpacing.toPx()

        val xLabelTextLayoutResults =
            visibleDataPoints.map {
                measurer.measure(
                    text = it.xLabel,
                    style = textStyle.copy(textAlign = TextAlign.Center),
                )
            }
        val maxXLabelWidth = xLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0
        val maxXLabelHeight = xLabelTextLayoutResults.maxOfOrNull { it.size.height } ?: 0
        val maxXLabelLineCount = xLabelTextLayoutResults.maxOfOrNull { it.lineCount } ?: 1
        val xLabelLineHeight = maxXLabelHeight / maxXLabelLineCount

        val viewPortHeightPx =
            size.height - (maxXLabelHeight + xLabelLineHeight + xLabelSpacingPx + 2 * verticalPaddingPx + 10f)
        val viewPortTopY = verticalPaddingPx + xLabelLineHeight + 10f

        val labelViewPortHeightPx = viewPortHeightPx + xLabelLineHeight

        val labelCountExclLast: Int =
            (labelViewPortHeightPx / (xLabelLineHeight + minLabelSpacingYPx)).toInt()

        val valueIncrement = (maxYValue - minYValue) / labelCountExclLast

        val yLabels =
            (0..labelCountExclLast).map {
                PriceLabel(
                    price = CurrencyAmount(maxYValue - it * valueIncrement, currency),
                )
            }

        val yLabelTextLayoutResults =
            yLabels.map {
                measurer.measure(
                    text = it.formatted(),
                    style = textStyle.copy(textAlign = TextAlign.Center),
                )
            }

        val maxYLabelWidth = yLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0
        val viewPortLeftX = 2f * horizontalPaddingPx + maxYLabelWidth
        val viewPortRightX = size.width - horizontalPaddingPx
        val heightRequiredForLabels = xLabelLineHeight * (labelCountExclLast + 1)
        val remainingHeightForLabels = labelViewPortHeightPx - heightRequiredForLabels
        val spaceBetweenLabels = remainingHeightForLabels / labelCountExclLast

        val viewPortBottomY = viewPortTopY + viewPortHeightPx
        val viewPort =
            Rect(
                left = viewPortLeftX,
                top = viewPortTopY,
                right = viewPortRightX,
                bottom = viewPortBottomY,
            )

        // drawRect(color = Color.Green, topLeft = viewPort.topLeft, size = viewPort.size)

        yLabelTextLayoutResults.forEachIndexed { index, textLayoutResult ->
            val yValue =
                viewPortTopY + index * (xLabelLineHeight + spaceBetweenLabels) - xLabelLineHeight / 2f
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft =
                    Offset(
                        x = horizontalPaddingPx + maxYLabelWidth - textLayoutResult.size.width,
                        y = yValue,
                    ),
                color = style.unselectedColor,
            )
            if (showHelperLines) {
                drawLine(
                    color = style.unselectedColor,
                    start =
                        Offset(
                            x = viewPortLeftX,
                            y = yValue + textLayoutResult.size.height / 2f,
                        ),
                    end =
                        Offset(
                            x = viewPortRightX,
                            y = yValue + textLayoutResult.size.height / 2f,
                        ),
                    strokeWidth = style.helperLinesThicknessPx,
                )
            }
        }

        xLabelWidth = maxXLabelWidth + xLabelSpacingPx
        xLabelTextLayoutResults.forEachIndexed { index, textLayoutResult ->
            val xValue = viewPortLeftX + xLabelSpacingPx / 2f + xLabelWidth * index
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft =
                    Offset(
                        x = xValue,
                        y = viewPortBottomY + xLabelSpacingPx,
                    ),
                color = if (index == selectedDataPointIndex) style.selectedColor else style.unselectedColor,
            )
            if (showHelperLines) {
                drawLine(
                    color = if (selectedDataPointIndex == index) style.selectedColor else style.unselectedColor,
                    start =
                        Offset(
                            x = xValue + textLayoutResult.size.width / 2f,
                            y = viewPortBottomY,
                        ),
                    end = Offset(x = xValue + textLayoutResult.size.width / 2f, y = viewPortTopY),
                    strokeWidth = if (selectedDataPointIndex == index) style.helperLinesThicknessPx * 2f else style.helperLinesThicknessPx,
                )
            }
            if (selectedDataPointIndex == index) {
                val priceLabel =
                    PriceLabel(price = CurrencyAmount(visibleDataPoints[index].y, currency))
                val valueResult =
                    measurer.measure(
                        text = priceLabel.formatted(),
                        style = textStyle.copy(color = style.selectedColor),
                        maxLines = 1,
                    )
                val textPositionX =
                    if (selectedDataPointIndex == visibleDataPoints.lastIndex) {
                        xValue - textLayoutResult.size.width
                    } else {
                        xValue - textLayoutResult.size.width / 2f
                    } + textLayoutResult.size.width / 2f
                val isTextInVisibleRange =
                    (size.width - textPositionX).roundToInt() in 0..size.width.roundToInt()

                if (isTextInVisibleRange) {
                    drawText(
                        textLayoutResult = valueResult,
                        topLeft =
                            Offset(
                                x = textPositionX,
                                y = viewPortTopY - valueResult.size.height - 10f,
                            ),
                    )
                }
            }
        }

        drawPoints =
            visibleDataPointIndices.map {
                val x =
                    viewPortLeftX + (it - visibleDataPointIndices.first) * xLabelWidth + xLabelWidth / 2f
                val ratio = (dataPoints[it].y - minYValue) / (maxYValue - minYValue)
                val y = viewPortBottomY - ratio * viewPortHeightPx
                DataPoint(x = x, y = y, xLabel = dataPoints[it].xLabel)
            }

        val conPoints1 = mutableListOf<DataPoint>()
        val conPoints2 = mutableListOf<DataPoint>()
        drawPoints.forEachIndexed { index, drawPoint ->
            if (index < drawPoints.lastIndex) {
                val p0 = drawPoint
                val p1 = drawPoints[index + 1]

                val x = (p1.x + p0.x) / 2f
                val y1 = p0.y
                val y2 = p1.y

                conPoints1.add(DataPoint(x = x, y = y1, xLabel = ""))
                conPoints2.add(DataPoint(x = x, y = y2, xLabel = ""))
            }
        }

        val linePath =
            Path().apply {
                if (drawPoints.isNotEmpty()) {
                    moveTo(drawPoints.first().x, drawPoints.first().y)
                    for (index in 1 until drawPoints.size) {
                        cubicTo(
                            x1 = conPoints1[index - 1].x,
                            y1 = conPoints1[index - 1].y,
                            x2 = conPoints2[index - 1].x,
                            y2 = conPoints2[index - 1].y,
                            x3 = drawPoints[index].x,
                            y3 = drawPoints[index].y,
                        )
                    }
                }
            }

        drawPath(
            path = linePath,
            color = style.chartLineColor,
            style =
                Stroke(
                    width = 5f,
                    cap = StrokeCap.Round,
                ),
        )

        drawPoints.forEachIndexed { index, dataPoint ->
            if (isShowingDataPoints) {
                drawCircle(
                    color = style.unselectedColor,
                    radius = 5f,
                    center = Offset(dataPoint.x, dataPoint.y),
                )
            }
            if (selectedDataPointIndex == index) {
                drawCircle(
                    color = style.selectedColor,
                    radius = 10f,
                    center = Offset(dataPoint.x, dataPoint.y),
                )
            }
        }
    }
}

private fun getSelectedDataPointIndex(
    touchOffsetX: Float,
    triggerWidth: Float,
    drawPoints: List<DataPoint>,
): Int {
    val triggerRangeLeft = touchOffsetX - triggerWidth / 2f
    val triggerRangeRight = touchOffsetX + triggerWidth / 2f
    return drawPoints.indexOfFirst { it.x in triggerRangeLeft..triggerRangeRight }
}

@Preview(widthDp = 1000)
@Composable
fun LineChartPreview() {
    CryptoTrackerTheme {
        val coinHistoryRandomized =
            (1..30).map {
                CoinPrice(
                    price =
                        CurrencyAmount(
                            Random.nextDouble() * 100.0,
                            Currency.getInstance(Locale.US),
                        ),
                    time = ZonedDateTime.now().plusHours(it.toLong()),
                )
            }
        val style =
            ChartStyle(
                chartLineColor = Color.Black,
                unselectedColor = Color(0xFF7C7C7C),
                selectedColor = Color.Black,
                helperLinesThicknessPx = 2f,
                axisLinesThicknessPx = 4f,
                labelFontSize = 4.sp,
                minYLabelSpacing = 25.dp,
                verticalPadding = 8.dp,
                horizontalPadding = 8.dp,
                xLabelSpacing = 8.dp,
            )
        val dataPoints =
            remember {
                coinHistoryRandomized.map {
                    DataPoint(
                        x = it.time.hour.toFloat(),
                        y = it.price.number.toFloat(),
                        xLabel = DateTimeFormatter.ofPattern("ha\nM/d").format(it.time),
                    )
                }
            }

        LineChart(
            dataPoints = dataPoints,
            currency = Currency.getInstance(Locale.US),
            style = style,
            visibleDataPointIndices = 0..29,
            modifier =
                Modifier
                    .width(700.dp)
                    .height(300.dp)
                    .background(Color.White),
            selectedDataPoint = dataPoints[1],
            showHelperLines = true,
        )
    }
}
