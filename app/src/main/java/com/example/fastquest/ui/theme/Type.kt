@file:OptIn(ExperimentalTextApi::class)

package com.example.fastquest.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.example.fastquest.R

val JosefinSans = FontFamily(
    Font(R.font.josefin_sans, FontWeight.Normal, variationSettings = FontVariation.Settings(FontVariation.weight(400))),
    Font(R.font.josefin_sans, FontWeight.Medium, variationSettings = FontVariation.Settings(FontVariation.weight(500))),
    Font(R.font.josefin_sans, FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontVariation.weight(600))),
    Font(R.font.josefin_sans, FontWeight.Bold, variationSettings = FontVariation.Settings(FontVariation.weight(700)))
)

// App typography using Josefin Sans across every Material3 text style
private val defaultTypography = Typography()
val Typography = defaultTypography.run {
    Typography(
        displayLarge = displayLarge.copy(fontFamily = JosefinSans),
        displayMedium = displayMedium.copy(fontFamily = JosefinSans),
        displaySmall = displaySmall.copy(fontFamily = JosefinSans),
        headlineLarge = headlineLarge.copy(fontFamily = JosefinSans),
        headlineMedium = headlineMedium.copy(fontFamily = JosefinSans),
        headlineSmall = headlineSmall.copy(fontFamily = JosefinSans),
        titleLarge = titleLarge.copy(fontFamily = JosefinSans),
        titleMedium = titleMedium.copy(fontFamily = JosefinSans),
        titleSmall = titleSmall.copy(fontFamily = JosefinSans),
        bodyLarge = bodyLarge.copy(fontFamily = JosefinSans),
        bodyMedium = bodyMedium.copy(fontFamily = JosefinSans),
        bodySmall = bodySmall.copy(fontFamily = JosefinSans),
        labelLarge = labelLarge.copy(fontFamily = JosefinSans),
        labelMedium = labelMedium.copy(fontFamily = JosefinSans),
        labelSmall = labelSmall.copy(fontFamily = JosefinSans)
    )
}
