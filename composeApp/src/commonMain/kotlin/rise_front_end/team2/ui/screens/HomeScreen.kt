package rise_front_end.team2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import rise_front_end.team2.ui.theme.AppTheme

@Composable
fun HomeScreen(
    onSyllabusClick: () -> Unit,  // Lambda to navigate to SyllabusScreen
    onFileHostingClick: () -> Unit, // Lambda to navigate to FileSharingScreen
    onStudentHelpForumClick: () -> Unit,
) {
    AppTheme {
        // Use Column to vertically arrange the buttons
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),  // Add padding around the content
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center content vertically
        ) {
            // Text at the top
            Text(text = "New HomeScreen")

            Spacer(modifier = Modifier.height(16.dp)) // Add space between the Text and buttons

            // Filled Button to navigate to SyllabusScreen
            Button(
                onClick = onSyllabusClick,
                modifier = Modifier.fillMaxWidth() // Make the button fill the width
            ) {
                Text("Go to Syllabus")
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add space between buttons

            // Tonal Button to navigate to FileSharingScreen
            FilledTonalButton(
                onClick = onFileHostingClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Go to File Sharing")
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add space between buttons

            // Elevated Button to navigate to Student Help Forum
            ElevatedButton(
                onClick = onStudentHelpForumClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Student Help Forum")
            }

        }

    }
}
