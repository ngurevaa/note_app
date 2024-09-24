package ru.kpfu.itis.gureva.mvi.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import ru.kpfu.itis.gureva.mvi.R

val bodyFontFamily = FontFamily(
    Font(R.font.wix_made_for_display_regular, FontWeight.Normal)
)

val titleFontFamily = FontFamily(
    Font(
        R.font.wix_made_for_display_medium, FontWeight.Medium
    )
)

val headerFontFamily = FontFamily(
    Font(R.font.wix_made_for_display_semibold, FontWeight.Bold)
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = headerFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = headerFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = headerFontFamily),
    headlineLarge = baseline.titleLarge.copy(fontFamily = headerFontFamily),
    headlineMedium = baseline.titleMedium.copy(fontFamily = headerFontFamily),
    headlineSmall = baseline.titleSmall.copy(fontFamily = headerFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = titleFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = titleFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = titleFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)
