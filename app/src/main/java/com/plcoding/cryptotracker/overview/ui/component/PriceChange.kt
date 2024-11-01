package com.plcoding.cryptotracker.overview.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import com.plcoding.cryptotracker.ui.theme.darkGreen

@Composable
fun PriceChange(changePercentage: String, isNegativeChange: Boolean) {
    val contentColor = if (isNegativeChange) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        Color.Green
    }
    Row(
        modifier =
            Modifier
                .clip(RoundedCornerShape(100f))
                .background(
                    color =
                        if (isNegativeChange) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            darkGreen
                        },
                ).padding(4.dp),
    ) {
        Icon(
            imageVector =
                if (isNegativeChange) {
                    Icons.Default.KeyboardArrowDown
                } else {
                    Icons.Default.KeyboardArrowUp
                },
            contentDescription = null,
            tint = contentColor,
        )
        Text(
            text = changePercentage,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            fontWeight = FontWeight.Medium,
            color = contentColor,
        )
    }
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
private fun PriceChangePreview() {
    CryptoTrackerTheme {
        Column {
            PriceChange("10%", false)
            PriceChange("10%", true)
        }
    }
}
