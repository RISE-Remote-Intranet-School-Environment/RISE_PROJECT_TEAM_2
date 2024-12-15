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
    // Observable state for the dark mode
    var isDarkMode by mutableStateOf(false)

    // Chooses the right colors according to the mode
    val colors
        get() = if (isDarkMode) DarkColorScheme else LightColorScheme
}

// Personalized Colors
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    onError = Neutral,
    onPrimary = OnPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    tertiary = TertiaryDark,
    onError = NeutralDark,
    onPrimary = OnPrimary
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    // Applies the colors for the present state
    MaterialTheme(
        colorScheme = AppTheme.colors,
        content = content
    )
}
