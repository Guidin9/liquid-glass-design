package com.example.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.app.R

/** iOS semantic system colors (light/dark). `tint` is the one app color used for selection and primary actions. */
@Immutable
data class SystemColors(
    val isDark: Boolean,
    val groupedBackground: Color,
    val cell: Color,
    val label: Color,
    val secondaryLabel: Color,
    val tertiaryLabel: Color,
    val separator: Color,
    val fill: Color,
    val segmentThumb: Color,
    val glassSurface: Color,
    val glassSelection: Color,
    val tint: Color,
    val green: Color,
    val blue: Color,
    val orange: Color,
    val red: Color,
)

fun lightSystemColors(tint: Color = Color(0xFF007AFF)) = SystemColors(
    isDark = false,
    groupedBackground = Color(0xFFF2F2F7),
    cell = Color(0xFFFFFFFF),
    label = Color(0xFF000000),
    secondaryLabel = Color(0x993C3C43),
    tertiaryLabel = Color(0x4D3C3C43),
    separator = Color(0x4A3C3C43),
    fill = Color(0x1F767680),
    segmentThumb = Color(0xFFFFFFFF),
    glassSurface = Color(0x66FAFAFA),
    glassSelection = Color(0x1A000000),
    tint = tint,
    green = Color(0xFF34C759),
    blue = Color(0xFF007AFF),
    orange = Color(0xFFFF9500),
    red = Color(0xFFFF3B30),
)

fun darkSystemColors(tint: Color = Color(0xFF0A84FF)) = SystemColors(
    isDark = true,
    groupedBackground = Color(0xFF000000),
    cell = Color(0xFF1C1C1E),
    label = Color(0xFFFFFFFF),
    secondaryLabel = Color(0x99EBEBF5),
    tertiaryLabel = Color(0x4DEBEBF5),
    separator = Color(0x99545458),
    fill = Color(0x3D767680),
    segmentThumb = Color(0xFF636366),
    glassSurface = Color(0x66121212),
    glassSelection = Color(0x1AFFFFFF),
    tint = tint,
    green = Color(0xFF30D158),
    blue = Color(0xFF0A84FF),
    orange = Color(0xFFFF9F0A),
    red = Color(0xFFFF453A),
)

val LocalSystemColors = staticCompositionLocalOf { lightSystemColors() }

// SF Pro is licensed for Apple platforms only; Inter is the closest open equivalent. Put Inter's variable font
// (Inter[opsz,wght].ttf from Google Fonts) at res/font/inter.ttf. The optical-size axis gives a text cut for
// body sizes and a display cut for large titles, like SF Text / SF Display.
@OptIn(ExperimentalTextApi::class)
private fun inter(weight: Int, opticalSize: Float) = Font(
    R.font.inter,
    FontWeight(weight),
    variationSettings = FontVariation.Settings(FontVariation.weight(weight), FontVariation.Setting("opsz", opticalSize)),
)

val InterText = FontFamily(inter(400, 16f), inter(500, 16f), inter(600, 16f), inter(700, 16f))
val InterDisplay = FontFamily(inter(400, 32f), inter(500, 32f), inter(600, 32f), inter(700, 32f))

/** iOS Dynamic Type default sizes, with Inter's tracking pulled in to sit closer to SF. */
object SystemType {
    val largeTitle = TextStyle(fontFamily = InterDisplay, fontWeight = FontWeight.Bold, fontSize = 34.sp, lineHeight = 41.sp, letterSpacing = (-0.6).sp)
    val title1 = TextStyle(fontFamily = InterDisplay, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 34.sp, letterSpacing = (-0.5).sp)
    val title2 = TextStyle(fontFamily = InterDisplay, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = (-0.4).sp)
    val title3 = TextStyle(fontFamily = InterDisplay, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 25.sp, letterSpacing = (-0.3).sp)
    val headline = TextStyle(fontFamily = InterText, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 22.sp, letterSpacing = (-0.3).sp)
    val body = TextStyle(fontFamily = InterText, fontWeight = FontWeight.Normal, fontSize = 17.sp, lineHeight = 22.sp, letterSpacing = (-0.3).sp)
    val callout = TextStyle(fontFamily = InterText, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 21.sp, letterSpacing = (-0.25).sp)
    val subheadline = TextStyle(fontFamily = InterText, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 20.sp, letterSpacing = (-0.2).sp)
    val footnote = TextStyle(fontFamily = InterText, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = (-0.1).sp)
    val caption1 = TextStyle(fontFamily = InterText, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp)
    val caption2 = TextStyle(fontFamily = InterText, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 13.sp)
    val tabLabel = TextStyle(fontFamily = InterText, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, lineHeight = 12.sp)
    val heroNumber = TextStyle(
        fontFamily = InterDisplay,
        fontWeight = FontWeight.SemiBold,
        fontSize = 46.sp,
        lineHeight = 50.sp,
        letterSpacing = (-1.0).sp,
        fontFeatureSettings = "tnum",
    )

    /** Merge into any style that shows live numbers so digits don't jitter as they change. */
    val figures = TextStyle(fontFeatureSettings = "tnum")
}

@Composable
fun LiquidGlassTheme(
    lightTint: Color = Color(0xFF007AFF),
    darkTint: Color = Color(0xFF0A84FF),
    content: @Composable () -> Unit,
) {
    val colors = if (isSystemInDarkTheme()) darkSystemColors(darkTint) else lightSystemColors(lightTint)
    // Material components that remain (text field cursor, ripples) only pick up the tint and surfaces.
    val scheme = (if (colors.isDark) darkColorScheme() else lightColorScheme()).copy(
        primary = colors.tint,
        background = colors.groupedBackground,
        surface = colors.cell,
        onSurface = colors.label,
        onBackground = colors.label,
    )
    CompositionLocalProvider(LocalSystemColors provides colors) {
        MaterialTheme(colorScheme = scheme, content = content)
    }
}
