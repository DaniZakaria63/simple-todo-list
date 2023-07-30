package com.daniza.simple.todolist.ui.theme

import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val md_theme_light_primary = Color(0xFF006C47)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFF8EF7C1)
val md_theme_light_onPrimaryContainer = Color(0xFF002113)
val md_theme_light_secondary = Color(0xFF4D6356)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFD0E8D7)
val md_theme_light_onSecondaryContainer = Color(0xFF0B1F15)
val md_theme_light_tertiary = Color(0xFF3C6472)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFC0E9FA)
val md_theme_light_onTertiaryContainer = Color(0xFF001F28)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFBFDF8)
val md_theme_light_onBackground = Color(0xFF191C1A)
val md_theme_light_surface = Color(0xFFFBFDF8)
val md_theme_light_onSurface = Color(0xFF191C1A)
val md_theme_light_surfaceVariant = Color(0xFFDCE5DC)
val md_theme_light_onSurfaceVariant = Color(0xFF404943)
val md_theme_light_outline = Color(0xFF707972)
val md_theme_light_inverseOnSurface = Color(0xFFEFF1ED)
val md_theme_light_inverseSurface = Color(0xFF2E312E)
val md_theme_light_inversePrimary = Color(0xFF71DAA6)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF006C47)
val md_theme_light_outlineVariant = Color(0xFFC0C9C1)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFF71DAA6)
val md_theme_dark_onPrimary = Color(0xFF003823)
val md_theme_dark_primaryContainer = Color(0xFF005235)
val md_theme_dark_onPrimaryContainer = Color(0xFF8EF7C1)
val md_theme_dark_secondary = Color(0xFFB4CCBC)
val md_theme_dark_onSecondary = Color(0xFF203529)
val md_theme_dark_secondaryContainer = Color(0xFF364B3F)
val md_theme_dark_onSecondaryContainer = Color(0xFFD0E8D7)
val md_theme_dark_tertiary = Color(0xFFA4CDDD)
val md_theme_dark_onTertiary = Color(0xFF053542)
val md_theme_dark_tertiaryContainer = Color(0xFF234C5A)
val md_theme_dark_onTertiaryContainer = Color(0xFFC0E9FA)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF191C1A)
val md_theme_dark_onBackground = Color(0xFFE1E3DF)
val md_theme_dark_surface = Color(0xFF191C1A)
val md_theme_dark_onSurface = Color(0xFFE1E3DF)
val md_theme_dark_surfaceVariant = Color(0xFF404943)
val md_theme_dark_onSurfaceVariant = Color(0xFFC0C9C1)
val md_theme_dark_outline = Color(0xFF8A938C)
val md_theme_dark_inverseOnSurface = Color(0xFF191C1A)
val md_theme_dark_inverseSurface = Color(0xFFE1E3DF)
val md_theme_dark_inversePrimary = Color(0xFF006C47)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFF71DAA6)
val md_theme_dark_outlineVariant = Color(0xFF404943)
val md_theme_dark_scrim = Color(0xFF000000)


val card_light_green_bg = md_theme_dark_primary
val card_light_green_text = md_theme_dark_onPrimary

val card_dark_green_bg = md_theme_dark_primaryContainer
val card_dark_green_text = md_theme_dark_onPrimaryContainer

val card_black_bg = md_theme_dark_surface
val card_black_text = md_theme_dark_onSurface

val card_grey_bg = md_theme_light_outline
val card_grey_text = md_theme_light_onPrimary

val seed = Color(0xFF00724B)

enum class CardColor(val value: Int) {
    NONE(0), LIGHT_GREEN(1), DARK_GREEN(2), BLACK(3), GREY(4),
}

@Composable
fun CardColor.parseAsBackground(): CardColors =
    when(this){
        CardColor.LIGHT_GREEN -> CardDefaults.cardColors(
            containerColor = card_light_green_bg,
            contentColor = card_light_green_text
        )
        CardColor.DARK_GREEN -> CardDefaults.cardColors(
            containerColor = card_dark_green_bg,
            contentColor = card_dark_green_text
        )
        CardColor.BLACK -> CardDefaults.cardColors(
            containerColor = card_black_bg,
            contentColor = card_black_text
        )
        CardColor.GREY -> CardDefaults.cardColors(
            containerColor = card_grey_bg,
            contentColor = card_grey_text
        )
        else -> CardDefaults.cardColors()
    }