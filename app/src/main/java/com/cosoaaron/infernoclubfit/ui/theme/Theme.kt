package com.cosoaaron.infernoclubfit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Definimos el esquema de colores oscuros
private val DarkColorScheme = darkColorScheme(
    primary = InfernoRed,
    secondary = InfernoOrange,
    background = InfernoBlack,
    surface = InfernoDarkGrey,
    onPrimary = InfernoWhite,
    onSecondary = InfernoWhite,
    onBackground = InfernoWhite,
    onSurface = InfernoWhite,
)

@Composable
fun InfernoClubfitTheme(
    content: @Composable () -> Unit
) {
    // Aplicamos siempre el esquema oscuro
    MaterialTheme(
        colorScheme = DarkColorScheme,
        // typography = Typography, // Si te da error esta línea, bórrala o coméntala
        content = content
    )
}