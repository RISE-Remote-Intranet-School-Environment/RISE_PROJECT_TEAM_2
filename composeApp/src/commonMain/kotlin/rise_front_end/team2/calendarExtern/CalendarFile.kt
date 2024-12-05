import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Folder
import androidx.compose.ui.Alignment

class CalendarFile {

    @Composable
    fun FilePicker(onFileSelected: (Uri) -> Unit) {
        // Launcher for file selection
        val filePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { onFileSelected(it) } // Pass the selected file's URI
        }

        // Button to open file picker
        Button(onClick = { filePickerLauncher.launch("application/json") },
                modifier = Modifier
                    .width(65.dp)
                    .height(40.dp)
                    .padding(0.dp)

            ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.FileOpen , contentDescription = "Folder Icon")
                Spacer(modifier = Modifier.fillMaxWidth())
                Text("Import JSON File")
            }
        }
    }

    fun handleSelectedFile(context: Context, uri: Uri) {
        // Open the InputStream from the selected URI
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.let {
            val jsonString = it.reader().readText()
            println(jsonString) // Process your JSON here
        }
    }
}

@Composable
fun CalendarScreen() {
    val context = LocalContext.current
    val calendarFile = CalendarFile()

    // Define what happens when a file is selected
    val onFileSelected: (Uri) -> Unit = { uri ->
        calendarFile.handleSelectedFile(context, uri) // Handle the selected file
    }

    // Use the FilePicker composable, passing the callback
    calendarFile.FilePicker(onFileSelected = onFileSelected)
}
