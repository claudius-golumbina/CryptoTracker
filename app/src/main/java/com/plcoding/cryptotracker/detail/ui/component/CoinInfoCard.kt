package com.plcoding.cryptotracker.detail.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import com.plcoding.cryptotracker.ui.theme.darkGreen

@Composable
fun CoinInfoCard(
    title: String,
    formattedText: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Card(
        modifier =
            modifier.padding(8.dp).shadow(
                elevation = 15.dp,
                shape = RectangleShape,
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary,
            ),
        shape = RectangleShape,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
    ) {
        AnimatedContent(
            targetState = icon,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            label = "IconAnimation",
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = it),
                contentDescription = null,
                modifier = Modifier.size(75.dp).padding(top = 16.dp),
                tint = contentColor,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedContent(
            targetState = formattedText,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            label = "ValueAnimation",
        ) {
            Text(
                text = it,
                style =
                    LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = contentColor,
                    ),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp),
            fontWeight = FontWeight.Light,
            color = contentColor,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@PreviewLightDark
@Composable
fun CoinInfoCardPreview() {
    CryptoTrackerTheme {
        CoinInfoCard(
            title = "BTC",
            formattedText = "$ 100.000,00",
            icon = com.plcoding.cryptotracker.R.drawable.dollar,
            contentColor = darkGreen,
        )
    }
}
