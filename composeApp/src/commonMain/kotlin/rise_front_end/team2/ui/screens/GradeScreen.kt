package rise_front_end.team2.ui.screens
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import rise_front_end.team2.ui.theme.AppTheme


@Composable
fun GradeScreen(){
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "GradeScreen")
        }
    }
}