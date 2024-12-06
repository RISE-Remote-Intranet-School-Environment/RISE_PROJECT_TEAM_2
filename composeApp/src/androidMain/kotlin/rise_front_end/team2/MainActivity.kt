package rise_front_end.team2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import rise_front_end.team2.navigation.PlatformNavigation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlatformNavigation()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}