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
import com.google.gson.Gson
import rise_front_end.team2.ui.screens.mapEventsToActivities


data class Event(val title: String, val time: String, val color: String, val date: String)

class CalendarFile {

    @Composable
    fun FilePicker(onFileSelected: (Uri) -> Unit) {
        val context = LocalContext.current
        System.out.println("IN fun FilePicker()")

        // File picker launcher
        val filePickerLauncher = rememberLauncherForActivityResult(

            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                System.out.println("File picker result: $uri")
                if (uri != null) {
                    System.out.println("File URI received (calendarFile.kts) : $uri")
                    onFileSelected(uri)
                } else {
                    System.out.println("No file selected.")
                }
            }
        )

        System.out.println(" after filePickerLauncher -- ")

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

    fun handleFileSelection(uri: Uri, context: Context) {
        try {
            println("Handling file selection for URI: $uri")
            // Assuming parseJsonFile is a method to handle the URI and parse the JSON file
            val events = parseJsonFile(uri, context)
            println("Events loaded: $events")

            // Log events for debugging
            events.forEach { event ->
                println("Event Details: ${event.date}, ${event.title}, ${event.color}")
            }

            // You can now map events to activities or any other logic you need
            //activities.clear() // Clear existing activities
            //activities.putAll(mapEventsToActivities(events)) // Populate with new activities

        } catch (e: Exception) {
            println("Error loading file: ${e.message}")
            e.printStackTrace()
        }
    }

    // In CalendarFile.kts
    fun parseJsonFile(uri: Uri, context: Context): List<Event> {
        System.out.println("IN fun parseJsonFile()")
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.let { stream ->
            // You can now parse the inputStream as a JSON file
            try {
                // Assuming you're using something like a JSON library to parse the input stream
                val jsonString = stream.bufferedReader().use { it.readText() }
                // Now, parse the JSON string to events (you can use your custom parsing logic)
                val events = parseEventsFromJson(jsonString)  // Assuming you have a parse method
                stream.close()
                return events
            } catch (e: Exception) {
                e.printStackTrace()
                System.err.println("Error reading or parsing the file: ${e.message}")
            }
        } ?: run {
            System.err.println("Failed to open input stream from URI: $uri")
        }
        return emptyList() // If it fails, return an empty list
    }

    fun parseEventsFromJson(json: String): List<Event> {
        val gson = Gson()
        return gson.fromJson(json, Array<Event>::class.java).toList() // Convert JSON to List<Event>
    }



}