package rise_front_end.team2.ui.screens

import android.app.TimePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import rise_front_end.team2.ui.theme.AppTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import kotlinx.serialization.json.Json

import CalendarFile
import Event



val modernGradientDarkColor = listOf(
    Color(0xFF6a7ddcL),
    Color(0xFFa357d9L),
    Color(0xFFd745a7L),
    Color(0xFFd43d31L),
    Color(0xFFc9ab28L),
    Color(0xFF64B823L)
)


@Composable
fun TimePicker
            (
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            onTimeSelected(hourOfDay, minute)
        },
        initialHour,
        initialMinute,
        true // Use 24-hour format
    ).apply {
        setOnDismissListener { onDismiss() }
        show()
    }
}

@Composable
fun EventList(events: List<Event>) {
    if (events.isEmpty()) {
        Text("No events to display", modifier = Modifier.padding(16.dp))
    } else {
        // Display the list of events
        events.forEach { event ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${event.date}: ${event.title}",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .width(16.dp)
                        .height(16.dp)
                        .background(Color(android.graphics.Color.parseColor(event.color)))
                )
            }
        }
    }
}


fun mapEventsToActivities(events: List<Event>): SnapshotStateMap<LocalDate, MutableList<Triple<String, String, Color>>> {
    val activities = mutableStateMapOf<LocalDate, MutableList<Triple<String, String, Color>>>()
    events.forEach { event ->
        val color = Color(android.graphics.Color.parseColor(event.color)) // Ensure color parsing is correct
        val date = LocalDate.parse(event.date) // Assuming date is in a valid format (e.g. "2024-12-06")
        val activity = Triple(event.title, event.time, color)

        activities.getOrPut(date) { mutableListOf() }.add(activity)
    }

    println("Mapped Activities: $activities") // Check the mapping of activities to dates
    System.out.print("Mapped Activities: $activities")
    return activities
}



@Composable
fun CalendarScreen() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
            var selectedDate by remember { mutableStateOf(LocalDate.now()) } // Track the selected date
            var showDialog by remember { mutableStateOf(false) } // State to control the dialog
            val context = LocalContext.current
            val calendarFile = CalendarFile()


            // Define a list of events that you can update once a file is selected
            var events by remember { mutableStateOf<List<Event>>(emptyList()) }

            val onFileSelected: (Uri) -> Unit = { uri ->
                System.out.println("File picker triggered with URI: $uri")
                if (uri != null) {
                    events = calendarFile.parseJsonFile(uri, context)
                    System.out.println("Events loaded: $events")
                } else {
                    System.out.println("No file selected.")
                }
            }

            // Show the events (for example)
            events.forEach { event ->
                Text(text = "${event.title} at ${event.time}")
            }

            // Map events to activities (this could also be done when events are first parsed)
            //val activities = mapEventsToActivities(events)
            val activities by remember { mutableStateOf(SnapshotStateMap<LocalDate, MutableList<Triple<String, String, Color>>>()) }


            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CalendarHeader(
                    currentMonth = selectedMonth,
                    onPreviousMonth = { selectedMonth = selectedMonth.minusMonths(1) },
                    onNextMonth = { selectedMonth = selectedMonth.plusMonths(1) }
                )

                CalendarView(
                    currentMonth = selectedMonth,
                    activities = activities,
                    selectedDate = selectedDate,
                    onDayClick = { date -> selectedDate = date },
                    events = events // Pass empty list for now
                )

                Spacer(modifier = Modifier.height(16.dp))

                ActivitiesList(
                    selectedDate = selectedDate,
                    activities = activities[selectedDate].orEmpty()
                )

                // Floating action button to add activity
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    FloatingActionButton(
                        onClick = { showDialog = true },
                        modifier = Modifier.padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Text("+", fontSize = 24.sp, color = Color.White, textAlign = TextAlign.Center)
                    }
                }
            }

            // Show dialog for adding activity
            if (showDialog) {
                ShowAddActivityDialog(
                    selectedDate = selectedDate,
                    activities = activities,
                    onDismiss = { showDialog = false }
                )
            }
        }
    }
}

@Composable
fun CalendarHeader(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val monthName = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val year = currentMonth.year

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = onPreviousMonth) {
            Text("<")
        }
        Text(
            text = "$monthName $year",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Button(onClick = onNextMonth) {
            Text(">")
        }
        CalendarFile().FilePicker(onFileSelected = {uri ->  })
    }
}


@Composable
fun CalendarView(
    currentMonth: YearMonth,
    activities: SnapshotStateMap<LocalDate, MutableList<Triple<String, String, Color>>>,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit,
    events: List<Event>
) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val daysInMonth = currentMonth.lengthOfMonth()
    val startDayOfWeek = (currentMonth.atDay(1).dayOfWeek.value + 5) % 7 // Adjust for Monday = 0
    val screenWidth =
        LocalContext.current.resources.displayMetrics.widthPixels / LocalContext.current.resources.displayMetrics.density
    val cellSize = (screenWidth / 7).dp

    System.out.print("Cell Size: $cellSize")

    Column(modifier = Modifier.fillMaxWidth()) {
        // Weekday headers without grid borders
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f) // Even spacing
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp)) // Space between weekdays and grid

        // Loop through the activities and display them in each day cell
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(daysInMonth) { day ->
                val date = currentMonth.atDay(day + 1)
                val activitiesForDate = activities[date].orEmpty()

                DayCell(
                    date = date,
                    activityCount = activitiesForDate.size,
                    isSelected = date == selectedDate,
                    onClick = { onDayClick(date) },
                    cellSize = cellSize,
                    activities = activitiesForDate // Pass complete activities list
                )
            }
        }
    }
}

@Composable
fun DayCell(
    date: LocalDate,
    activityCount: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    cellSize: Dp,
    activities: List<Triple<String, String, Color>> // Liste des activités (titre, heure, couleur)
) {
    // Trier les activités par leur heure de début (heure:min)
    val sortedActivities = activities.sortedBy { it.second }

    Box(
        modifier = Modifier
            .size(cellSize) // Taille fixe pour la cellule
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                shape = MaterialTheme.shapes.small
            )
            .clickable { onClick() }
    ) {
        // Numéro du jour
        Text(
            text = date.dayOfMonth.toString(),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(2.dp)
                .zIndex(1f)
        )

        // Lignes des activités (affichées dans l'ordre trié)
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp), // Espacement entre les lignes
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 3.dp)
                .zIndex(0f) // Pour afficher les lignes au-dessus du numéro du jour
                .align(Alignment.Center)
        ) {
            sortedActivities.forEach { (_, time, color) ->
                Box(
                    modifier = Modifier
                        .height(4.dp) // Hauteur de la ligne
                        .fillMaxWidth()
                        .background(color, shape = RoundedCornerShape(50)) // Ligne arrondie
                )
            }
        }
    }
}



@Composable
fun ActivitiesList(selectedDate: LocalDate, activities: List<Triple<String, String, Color>>) {
    val sortedActivities = activities.sortedBy { it.second } // Sort by time

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        if (sortedActivities.isEmpty()) {
            Text(
                text = "No activities for this day.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp),
                textAlign = TextAlign.Start
            )
        } else {
            sortedActivities.forEach { (activity, time, color) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(color, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "$time - $activity",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                }

                Divider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    thickness = 1.dp
                )
            }
        }
    }
}


@Composable
fun ShowAddActivityDialog(
    selectedDate: LocalDate,
    activities: SnapshotStateMap<LocalDate, MutableList<Triple<String, String, Color>>>, // Update type to include color
    onDismiss: () -> Unit
) {
    var activityText by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color.Blue) } // Default color

    // Show the time picker if triggered
    if (showTimePicker) {
        TimePicker(
            initialHour = 12,
            initialMinute = 0,
            onTimeSelected = { hour, minute ->
                selectedTime = String.format("%02d:%02d", hour, minute)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false },
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Activity \n $selectedDate") },
        text = {
            Column {
                // Activity Name
                OutlinedTextField(
                    value = activityText,
                    onValueChange = { activityText = it },
                    label = { Text("Enter Activity") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Time Picker
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Time: ", style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = { showTimePicker = true }) {
                        Text(if (selectedTime.isEmpty()) "Pick Time" else selectedTime)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Color Picker
                Text("Choose a Color:", style = MaterialTheme.typography.bodyMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val colors = modernGradientDarkColor
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(color, shape = MaterialTheme.shapes.small)
                                .border(
                                    width = if (color == selectedColor) 2.dp else 1.dp,
                                    color = if (color == selectedColor) Color.Black else Color.Gray,
                                    shape = MaterialTheme.shapes.small
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(32.dp))

                Button(onClick = {
                    if (activityText.isNotBlank()) {
                        val time = if (selectedTime.isNotEmpty()) " at $selectedTime" else ""
                        val activityWithTime = "$activityText$time"

                        activities.getOrPut(selectedDate) { mutableListOf() }
                            .add(Triple(activityText, selectedTime, selectedColor))
                    }
                    onDismiss()
                }) {
                    Text("Save")
                }
            }
        }
    )
}