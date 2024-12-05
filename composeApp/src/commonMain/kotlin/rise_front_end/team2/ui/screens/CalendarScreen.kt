package rise_front_end.team2.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.zIndex
import rise_front_end.team2.ui.theme.AppTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*


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
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.small
            )
            .clickable { onClick() }
    ) {
        // Day number in the upper-left corner
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f)
                .padding(4.dp),
            contentAlignment = Alignment.TopStart // Align to the top-left
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(2.dp)
                    .size(20.dp) // Circle size
                    .background(MaterialTheme.colorScheme.background , shape = CircleShape) // Circular background
                    //.border(0.dp, MaterialTheme.colorScheme.onBackground, shape = CircleShape) // Optional border
                    .zIndex(1f) // Ensure it stays on top
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    color = MaterialTheme.colorScheme.primary ,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Center ) // Center the day number inside the circle
                )
            }
        }

        // Activity indicators stacked from the top
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 4.dp), // Padding to maintain spacing from edges
        ) {
            for (i in 0 until minOf(activityCount, 10)) {
                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .fillMaxWidth()
                        .background(
                            color = activityColors[i % activityColors.size],
                            shape = RoundedCornerShape(50))
                        .padding(top = 3.dp) // Add some spacing between lines
                        .zIndex(0f)
                )
                if (i < activityCount - 1) {
                    Spacer(modifier = Modifier.height(1.dp)) // Add space between activities
                }
            }
        }
    }
}


@Composable
fun ActivitiesList(selectedDate: LocalDate, activities: List<Pair<String, Color>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        
        Text(
            text = selectedDate.format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy")),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start 
        )

        
        if (activities.isEmpty()) {
            Text(
                text = "No activities for this day.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp),
                textAlign = TextAlign.Start
            )
        } else {
            
            activities.forEach { (activity, color) ->
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
                        text = activity,
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