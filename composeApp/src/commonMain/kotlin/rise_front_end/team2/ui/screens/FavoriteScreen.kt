package rise_front_end.team2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import rise_front_end.team2.ui.theme.AppTheme

@Composable
fun FavoriteScreen() {
    AppTheme {
        // Create a coroutine scope to show the snackbar on button click
        val scope = rememberCoroutineScope()

        // SnackbarHostState to control the snackbar
        val snackbarHostState = remember { SnackbarHostState() }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Favorite Screen", style = MaterialTheme.typography.titleLarge)
                Text(text= "This is just to show you some buttons and how to implement them in", style = MaterialTheme.typography.bodyMedium)

                // Button
                Button(onClick = { /* Do nothing, it's just for show */ }) {
                    Text("Click Me")
                }

                // TextField
                var text by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Enter text") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Show Snackbar button
                Button(onClick = {
                    // Launch a coroutine to show the snackbar
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "This is a Snackbar message",
                            actionLabel = "Dismiss"
                        )
                    }
                }) {
                    Text("Show Snackbar")
                }

                // Switch
                var isChecked by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Toggle option")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                }

                // Slider
                var sliderPosition by remember { mutableStateOf(0.5f) }
                Text("Adjust value")
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // SnackbarHost positioned at the bottom
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
