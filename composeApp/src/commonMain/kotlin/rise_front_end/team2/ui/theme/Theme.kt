package rise_front_end.team2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicMaterialThemeState
import rise_front_end.team2.ui.theme.AppTheme.isDarkMode

object AppTheme {
    // État observable pour le mode sombre
    var isDarkMode by mutableStateOf(false)

}



@Composable
fun AppTheme(
    darkTheme: Boolean = isDarkMode,
    content: @Composable () -> Unit,
) {
    val dynamicThemeState = rememberDynamicMaterialThemeState(
        isDark = darkTheme,
        style = PaletteStyle.Expressive,
        primary = Primary,
        secondary = Secondary,
        tertiary = Tertiary,
    )

    DynamicMaterialTheme(
        state = dynamicThemeState,
        animate = true,
        content = content,
    )
}