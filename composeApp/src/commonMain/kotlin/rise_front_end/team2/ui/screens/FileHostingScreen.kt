// CommonMain/FileSharingScreen.kt
package rise_front_end.team2.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import rise_front_end.team2.ui.theme.AppTheme

@Composable
fun FileHostingScreen() {
    AppTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "FileHostingScreen")
        }
    }
}
