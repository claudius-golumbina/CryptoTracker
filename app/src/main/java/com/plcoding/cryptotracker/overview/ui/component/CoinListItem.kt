package com.plcoding.cryptotracker.overview.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.plcoding.cryptotracker.overview.domain.model.CoinId
import com.plcoding.cryptotracker.overview.ui.model.CoinUiModel
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinListItem(
    coinUiModel: CoinUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
        modifier
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = coinUiModel.iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(85.dp)
        )
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = coinUiModel.symbol,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = coinUiModel.name,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = coinUiModel.price,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            PriceChange(coinUiModel.changePercentage, coinUiModel.isNegativeChange)
        }
    }
}

@Preview(showBackground = true)
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun CoinListItemPreview() {
    CryptoTrackerTheme {
        CoinListItem(coinUiModel = previewModel, onClick = {})
    }
}

val previewModel = CoinUiModel(
    id = CoinId("bitcoin"),
    name = "Bitcoin",
    symbol = "BTC",
    marketCap = "$1000000",
    price = "$100",
    changePercentage = "10%",
    isNegativeChange = false,
    changeAmount = "$10",
    iconResId = com.plcoding.cryptotracker.R.drawable.btc
)
