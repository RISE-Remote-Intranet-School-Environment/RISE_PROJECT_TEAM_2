package rise_front_end.team2.ui.screens.calendar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class Event(val title: String, val time: String, val color: String, val date: String)

public fun parseJsonFile(uri: Uri, context: Context): List<Event> {
    val events = mutableListOf<Event>()
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val jsonString = inputStream?.bufferedReader()?.use { it.readText() } ?: ""

        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val date = jsonObject.getString("date")
            val title = jsonObject.getString("title")
            val time = jsonObject.getString("time")
            val color = jsonObject.getString("color")
            events.add(Event(title, time, color, date))
        }
    } catch (e: Exception) {
        Log.e("parseJsonFile", "Error parsing JSON file: ${e.message}", e)
        e.printStackTrace()
    }
    return events
}

fun mapEventsToActivities(events: List<Event>): SnapshotStateMap<LocalDate, MutableList<Triple<String, String, Color>>> {
    val activities = mutableStateMapOf<LocalDate, MutableList<Triple<String, String, Color>>>()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    events.forEach { event ->
        try {
            val color = Color(android.graphics.Color.parseColor(event.color))

            val date = LocalDate.parse(event.date, formatter)
            val activity = Triple(event.title, event.time, color)

            activities.getOrPut(date) { mutableListOf() }.add(activity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return activities
}

@Composable
fun FilePicker(onFileSelected: (Uri) -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                try {
                    val contentResolver = context.contentResolver
                    val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    contentResolver.takePersistableUriPermission(uri, takeFlags)

                    // Call your function to handle the file and update the activities
                    onFileSelected(uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    )

    Button(
        onClick = {
            launcher.launch(arrayOf("application/json"))
        },
        modifier = Modifier.padding(4.dp)
            .width(50.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.FileOpen, contentDescription = "File Picker Icon", modifier = Modifier.size(24.dp))
        }
    }
}

fun addActivitiesFromJson(
    uri: Uri,
    context: Context,
    existingActivities: SnapshotStateMap<LocalDate, MutableList<Triple<String, String, Color>>>
) {
    // Step 1: Parse the new events from the JSON file
    val events = parseJsonFile(uri, context)

    // Step 2: Map the events to activities and merge them with existing activities
    val newActivities = mapEventsToActivities(events)

    // Step 3: Merge the new activities with the existing ones
    newActivities.forEach { (date, activitiesForDate) ->
        // Get the current list of activities for the date or an empty list if none exist
        val currentActivities = existingActivities.getOrPut(date) { mutableListOf() }

        // Merge the new activities with the current ones (no duplicates)
        currentActivities.addAll(activitiesForDate)
    }

    // Step 4: Update the state with the merged activities (if necessary, depending on your UI structure)
    // For example, you can update the activities state here
}
