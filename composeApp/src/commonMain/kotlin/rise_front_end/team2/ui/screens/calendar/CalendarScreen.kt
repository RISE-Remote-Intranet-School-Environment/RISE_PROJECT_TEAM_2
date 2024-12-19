package rise_front_end.team2.ui.screens.calendar

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
import rise_front_end.team2.ui.theme.AppTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.compose.material.icons.Icons

import android.content.Intent
import android.util.Log
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Search
import kotlinx.datetime.number
import network.chaintech.kmp_date_time_picker.parse
import rise_front_end.team2.ui.theme.*

// Enum for Display Mode
enum class DisplayMode {
    MONTH, WEEK
}

fun onDisplayModeChange(displayMode: MutableState<DisplayMode>) {
    displayMode.value = if (displayMode.value == DisplayMode.MONTH) DisplayMode.WEEK else DisplayMode.MONTH
}

fun getStartOfWeek(date: LocalDate): LocalDate {
    val dayOfWeek = date.dayOfWeek.value
    return date.minusDays((dayOfWeek - 1).toLong())
}

@Composable
fun SearchBar(
    onDismiss: () -> Unit
) {
    var searchText by remember { mutableStateOf("") } // Track user input

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Activities") },
            modifier = Modifier.weight(1f) // Take up available width
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onDismiss) {
            Icon(Icons.Filled.Check , contentDescription = "Enter", modifier = Modifier.size(24.dp, 24.dp))
        }
    }
}



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
fun CalendarScreen() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            // State variables
            var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
            var selectedDate by remember { mutableStateOf(LocalDate.now()) }
            var showDialog by remember { mutableStateOf(false) }
            var searchMode by remember { mutableStateOf(false) } // Search Mode state
            val displayMode = remember { mutableStateOf(DisplayMode.MONTH) } // Display Mode state

            val context = LocalContext.current
            var events by remember { mutableStateOf<List<Event>>(emptyList()) }
            val activities by remember { mutableStateOf(SnapshotStateMap<LocalDate, MutableList<Triple<String, String, Color>>>()) }

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Pass states and handlers to CalendarHeader
                CalendarHeader(
                    currentMonth = selectedMonth,
                    onPreviousMonth = { selectedMonth = selectedMonth.minusMonths(1) },
                    onNextMonth = { selectedMonth = selectedMonth.plusMonths(1) },
                    onFileSelected = { uri ->
                        events = parseJsonFile(uri, context)
                        activities.putAll(mapEventsToActivities(events))
                    },
                    displayMode = displayMode,
                    searchMode = searchMode,
                    onSearchModeChange = { searchMode = it },
                )

                CalendarView(
                    currentMonth = selectedMonth,
                    activities = activities,
                    selectedDate = selectedDate,
                    onDayClick = { selectedDate = it },
                    events = events,
                    displayMode = displayMode.value,
                    weekStartDate = getStartOfWeek(selectedDate)
                )

                Spacer(modifier = Modifier.height(1.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.Start
                    )

                    // Floating Action Button to toggle displayMode
                    FloatingActionButton(
                        onClick = {
                            // Toggle displayMode and hide the search bar
                            searchMode = false // Ensure search mode is hidden
                            displayMode.value =
                                if (displayMode.value == DisplayMode.MONTH) DisplayMode.WEEK
                                else DisplayMode.MONTH
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(start = 80.dp)
                    ) {
                        val icon = if (displayMode.value == DisplayMode.MONTH) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown
                        Icon(icon, contentDescription = "Toggle Display Mode", modifier = Modifier.size(35.dp), tint = MaterialTheme.colorScheme.surface)
                    }

                    FloatingActionButton(
                        onClick = { showDialog = true },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Text("+", fontSize = 24.sp, color = MaterialTheme.colorScheme.surface, textAlign = TextAlign.Center)
                    }
                }

                // Conditionally show the SearchBar or ActivitiesList
                if (searchMode) {
                    SearchBar(
                        onDismiss = { searchMode = false }
                    )
                } else {
                    ActivitiesList(
                        selectedDate = selectedDate,
                        activities = activities[selectedDate].orEmpty()
                    )
                }
            }

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
    onNextMonth: () -> Unit,
    onFileSelected: (Uri) -> Unit,
    displayMode: MutableState<DisplayMode>,
    searchMode: Boolean,
    onSearchModeChange: (Boolean) -> Unit
) {
    val month = currentMonth.month.number
    val year = currentMonth.year
    val monthName = LocalDate.of(year, month, 1).format(DateTimeFormatter.ofPattern("MMM"))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onPreviousMonth,
            modifier = Modifier.padding(4.dp).width(50.dp),
            contentPadding = PaddingValues(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "previous month", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.surface)
        }

        Text(
            text = "$monthName $year",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium)

        Button(
            onClick = onNextMonth,
            modifier = Modifier.padding(4.dp).width(50.dp),
            contentPadding = PaddingValues(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "next month", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.surface)
        }

        FilePicker(onFileSelected = onFileSelected)

        Button(
            onClick = {
                // Toggle searchMode and optionally switch to WEEK mode
                onSearchModeChange(!searchMode)
                displayMode.value = DisplayMode.WEEK
            },
            modifier = Modifier.padding(4.dp).width(50.dp),
            contentPadding = PaddingValues(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(Icons.Filled.Search, contentDescription = "Search Button", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.surface)
        }
    }
}

@Composable
fun CalendarView(
    currentMonth: YearMonth,
    activities: SnapshotStateMap<LocalDate, MutableList<Triple<String, String, Color>>>,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit,
    events: List<Event>,
    displayMode: DisplayMode,
    weekStartDate: LocalDate
) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val daysInMonth = currentMonth.lengthOfMonth()
    val startDayOfWeek = (currentMonth.atDay(1).dayOfWeek.value + 6) % 7 // Adjust for Monday = 0
    val screenWidth =
        LocalContext.current.resources.displayMetrics.widthPixels / LocalContext.current.resources.displayMetrics.density
    val cellSize = (screenWidth / 7).dp


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

        when (displayMode) {
            DisplayMode.MONTH -> {
                // Display entire month grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Fill empty cells before the first day of the month
                    items(startDayOfWeek) {
                        Box(modifier = Modifier.size(cellSize))
                    }

                    // Display all days in the month
                    items(daysInMonth) { day ->
                        val date = currentMonth.atDay(day + 1)
                        val activitiesForDate = activities[date].orEmpty()

                        DayCell(
                            date = date,
                            activityCount = activitiesForDate.size,
                            isSelected = date == selectedDate,
                            onClick = { onDayClick(date) },
                            cellSize = cellSize,
                            activities = activitiesForDate
                        )
                    }
                }
            }

            DisplayMode.WEEK -> {
                // Display only the selected week
                val daysInWeek = (0 until 7).map { weekStartDate.plusDays(it.toLong()) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // This evenly spaces the days
                ) {
                    daysInWeek.forEach { date ->
                        val activitiesForDate = activities[date].orEmpty()

                        DayCell(
                            date = date,
                            activityCount = activitiesForDate.size,
                            isSelected = date == selectedDate,
                            onClick = { onDayClick(date) },
                            cellSize = (screenWidth/8).dp,
                            activities = activitiesForDate
                        )
                    }
                }
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
    activities: List<Triple<String, String, Color>>
) {

    val sortedActivities = activities.sortedBy { it.second }

    Box(
        modifier = Modifier
            .size(cellSize)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                shape = MaterialTheme.shapes.small
            )
            .clickable { onClick() }
    ) {
        // Use a Row to control positioning with Spacer for alignment
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart) // Align to the top left of the cell
                .padding(4.dp)
        ) {
            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = date.dayOfMonth.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 4.dp) // Optional padding to fine-tune position
            )
        }
        // Activities displayed below the day number
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp), // Space between activity indicators
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, start = 4.dp, end = 4.dp, bottom = 0.dp) // Space for day number
                .align(Alignment.Center) // Center the activities vertically within the Box
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            sortedActivities.forEach { (_, time, color) ->
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth(0.8f) // Bars smaller than full width
                        .background(color, shape = RoundedCornerShape(50))
                        .align(Alignment.CenterHorizontally) // Center the bars horizontally
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

                HorizontalDivider(
                    color = Primary.copy(alpha = 0.1f),
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