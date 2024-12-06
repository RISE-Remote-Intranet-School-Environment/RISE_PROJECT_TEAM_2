package rise_front_end.team2.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import rise_front_end.team2.ui.theme.AppTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import network.chaintech.*


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
            val activities = remember { mutableStateMapOf<LocalDate, MutableList<String>>() } // Stores activities for dates
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
    activities: Map<LocalDate, List<String>>,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit
) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val daysInMonth = currentMonth.lengthOfMonth()
    val startDayOfWeek = (currentMonth.atDay(1).dayOfWeek.value + 5) % 7 // Adjust for Monday = 0

    Column {
        // Weekday headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Days grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(4.dp)
        ) {
            // Empty cells at the start of the month
            items(startDayOfWeek) {
                Spacer(modifier = Modifier.size(40.dp))
            }

            // Days of the month
            items(daysInMonth) { day ->
                val date = currentMonth.atDay(day + 1)

                DayCell(
                    date = date,
                    activityCount = activities[date]?.size ?: 0,
                    isSelected = date == selectedDate,
                    onClick = { onDayClick(date) }
                )
            }
        }
    }
}

@Composable
fun DayCell(date: LocalDate, activityCount: Int, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.small
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = date.dayOfMonth.toString(),
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 16.sp
            )
            if (activityCount > 0) {
                Text(
                    text = "•",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ActivitiesList(selectedDate: LocalDate, activities: List<String>) {
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
            activities.forEach { activity ->
                Text(
                    text = "• $activity",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}


@Composable
fun ShowAddActivityDialog(
    selectedDate: LocalDate,
    activities: MutableMap<LocalDate, MutableList<String>>,
    onDismiss: () -> Unit
) {
    var activityText by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }

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
                OutlinedTextField(
                    value = activityText,
                    onValueChange = { activityText = it },
                    label = { Text("Enter Activity") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Time: ", style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = { showTimePicker = true }) {
                        Text(if (selectedTime.isEmpty()) "Pick Time" else selectedTime)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (activityText.isNotBlank()) {
                    val time = if (selectedTime.isNotEmpty()) " at $selectedTime" else ""
                    val activityWithTime = "$activityText$time"
                    activities.getOrPut(selectedDate) { mutableListOf() }.add(activityWithTime)
                }
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}