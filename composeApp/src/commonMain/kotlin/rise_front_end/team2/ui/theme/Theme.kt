package rise_front_end.team2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

object AppTheme {
    // État observable pour le mode sombre
    var isDarkMode by mutableStateOf(false)

    // Détermine les couleurs en fonction de l'état
    val colors
        get() = if (isDarkMode) DarkColorScheme else LightColorScheme
}

// Couleurs personnalisées
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    onError = Neutral
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    tertiary = TertiaryDark,
    onError = NeutralDark
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    // Applique les couleurs en fonction de l'état actuel
    MaterialTheme(
        colorScheme = AppTheme.colors,
        content = content
    )
}
