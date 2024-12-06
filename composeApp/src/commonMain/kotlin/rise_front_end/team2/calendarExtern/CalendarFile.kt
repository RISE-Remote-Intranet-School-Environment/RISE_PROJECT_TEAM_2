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
    @Composable
    fun FilePicker(onFileSelected: (Uri) -> Unit) {

        val filePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                System.out.println("File picker result: $uri")
                if (uri != null) {
                    onFileSelected(uri)
                } else {
                    System.out.println("No file selected.")
                }
            }
        )
        // Button to open file picker
        Button(onClick = { System.out.println("button clicked. Launching file picker")
            filePickerLauncher.launch("application/json") },
            modifier = Modifier
                .width(65.dp)
                .height(40.dp)
                .padding(0.dp)
        ) {
            System.out.println("Button Displayed")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.FileOpen , contentDescription = "Folder Icon")
                Spacer(modifier = Modifier.fillMaxWidth())
                Text("Import JSON File")
            }
        }
    }

    fun parseJsonFile(uri: Uri, context: Context): List<Event> {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

        return inputStream?.let {
            val jsonString = it.reader().readText()
            println("Loaded JSON: $jsonString") // Print the JSON content
            System.out.println("Loaded JSON: $jsonString")

            // Parse the JSON into a list of Event objects
            try {
                val events = Json.decodeFromString<List<Event>>(jsonString)
                println("Parsed Events: $events") // Print the parsed events
                System.out.println("Events: $events")
                events
            } catch (e: Exception) {
                println("Error parsing JSON: $e") // Handle error and print
                System.out.println("Error parsing JSON: $e")
                emptyList<Event>() // Return an empty list in case of error
            }
        } ?: emptyList()
    }

}


