package rise_front_end.team2.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import java.time.format.TextStyle
import java.util.*


val modernGradientDarkColor = listOf(
    Color(0xFFEF4444L), // Coral Red (Urgent task)
    Color(0xFF2563EBL), // Royal Blue (Work deadline)
    Color(0xFF9333EAL), // Deep Violet (Team collaboration)
    Color(0xFF10B981L),  // Minty Teal (Focus task)
    Color(0xFFDFAF2CL)   //Yellow Ocre
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
fun CalendarScreen() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
            val activities = remember { mutableStateMapOf<LocalDate, MutableList<Pair<String, Color>>>() } // Stores activities for dates
            var selectedDate by remember { mutableStateOf(LocalDate.now()) } // Track the selected date
            var showDialog by remember { mutableStateOf(false) } // State to control the dialog

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
                    onDayClick = { date -> selectedDate = date }
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
                        modifier = Modifier.padding(16.dp)
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
    }
}


@Composable
fun CalendarView(
    currentMonth: YearMonth,
    activities: SnapshotStateMap<LocalDate, MutableList<Pair<String, Color>>>,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit
) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val daysInMonth = currentMonth.lengthOfMonth()
    val startDayOfWeek = (currentMonth.atDay(1).dayOfWeek.value + 5) % 7 // Adjust for Monday = 0
    val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels / LocalContext.current.resources.displayMetrics.density
    val cellSize = (screenWidth / 7).dp


    Column (modifier = Modifier.fillMaxWidth()){
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

        // Grid for days
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(0.dp), // Ensure no extra space
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.fillMaxWidth()
        )
 {
            // Empty cells for alignment at the start of the month
            items(startDayOfWeek) {
                Spacer(modifier = Modifier.size(cellSize)) // Same size as day cells, no border
            }

            // Days of the month
            items(daysInMonth) { day ->
                val date = currentMonth.atDay(day + 1)
                val activitiesForDate = activities[date].orEmpty()
                DayCell(
                    date = date,
                    activityCount = activities[date]?.size ?: 0,
                    isSelected = date == selectedDate,
                    onClick = { onDayClick(date) },
                    cellSize = cellSize,
                    activityColors = activitiesForDate.map {it.second}
                )
            }
        }
    }
}

@Composable
fun DayCell(date: LocalDate,
            activityCount: Int,
            isSelected: Boolean,
            onClick: () -> Unit,
            cellSize: Dp,
            activityColors: List<Color> = modernGradientDarkColor ){
    Box(
        modifier = Modifier
            .size(cellSize) // Use consistent size for cells
            //.border(shape = MaterialTheme.shapes.small) // Optional border
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.small
            )
            .clickable { onClick() }
    ) {
        // Day number in the upper-left corner
        Box(
            modifier = Modifier
                .fillMaxSize() // Use the full size of the cell for positioning
                .padding(4.dp), // Add some padding to keep it away from edges
            contentAlignment = Alignment.TopStart // Align to the top-left
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp // Smaller font size for the corner
            )
        }

        // Activity indicator centered below
        if (activityCount > 0) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 4.dp, vertical = 2.dp)

            ){
                for (i in 0 until minOf(activityCount, 5)){
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(activityColors[i % activityColors.size])
                    )
                }

            }
        }
    }
}

@Composable
fun ActivitiesList(selectedDate: LocalDate, activities: List<Pair<String,Color>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Text(
            text = selectedDate.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (activities.isEmpty()) {
            Text("No activities for this day.", style = MaterialTheme.typography.bodyMedium)
        } else {
            activities.forEach { (activity, color) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(color, shape = MaterialTheme.shapes.small)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = activity,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@Composable
fun ShowAddActivityDialog(
    selectedDate: LocalDate,
    activities: MutableMap<LocalDate, MutableList<Pair<String, Color>>>, // Update type to include color
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
                        // Save activity with chosen color
                        activities.getOrPut(selectedDate) { mutableListOf() }
                            .add(activityWithTime to selectedColor)
                    }
                    onDismiss()
                }) {
                    Text("Save")
                }
            }
        }
    )
}
