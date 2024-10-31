package rise_front_end.team2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicMaterialThemeState

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val dynamicThemeState = rememberDynamicMaterialThemeState(
        isDark = darkTheme,
        style = PaletteStyle.TonalSpot,
        primary = Primary,
        secondary = Secondary,
        tertiary = Tertiary,
        neutral = Neutral,
    )

    DynamicMaterialTheme(
        state = dynamicThemeState,
        animate = true,
        content = content,
    )
}
