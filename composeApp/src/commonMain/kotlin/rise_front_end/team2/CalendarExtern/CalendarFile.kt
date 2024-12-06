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
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

data class Event(val title: String, val time: String, val color: String, val date: String)

class CalendarFile {

    // Button for file picker
// File picker button
    @Composable
    fun FilePicker(onFileSelected: (Uri) -> Unit) {
        val context = LocalContext.current

        // File picker launcher
        val filePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                System.out.println("File picker result: $uri")
                if (uri != null) {
                    System.out.println("File URI received: $uri")
                    onFileSelected(uri)
                } else {
                    System.out.println("No file selected.")
                }
            }
        )

        // Button to open file picker
        Button(
            onClick = {
                System.out.println("Button clicked. Launching file picker.")
                filePickerLauncher.launch("application/json")
            },
            modifier = Modifier
                .width(200.dp) // Adjust button size
                .height(50.dp)
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.FileOpen, contentDescription = "File Picker Icon")
                Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                Text("Import JSON File")
            }
        }
    }

    fun parseJsonFile(uri: Uri, context: Context): List<Event> {
        System.out.println("Starting parseJsonFile for URI: $uri")
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

        return inputStream?.let {
            val jsonString = it.reader().readText()
            System.out.println("Loaded JSON: $jsonString") // Print JSON content

            try {
                // Attempt to parse JSON
                val events = Json.decodeFromString<List<Event>>(jsonString)
                System.out.println("Parsed Events: $events") // Print parsed events
                events
            } catch (e: Exception) {
                // Log the error and return an empty list
                System.out.println("Error parsing JSON: $e")
                emptyList<Event>()
            }
        } ?: run {
            System.out.println("InputStream is null. Unable to read the file.")
            emptyList()
        }
    }

}