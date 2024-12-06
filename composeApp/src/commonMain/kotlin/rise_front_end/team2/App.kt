package rise_front_end.team2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rise_front_end.team2.navigation.PlatformNavigation

import rise_front_end.team2.ui.theme.AppTheme // Corrected import statement
import rise_front_end_team2.composeapp.generated.resources.Res
import rise_front_end_team2.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    AppTheme {
        PlatformNavigation()
    }
}