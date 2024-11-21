package rise_front_end.team2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rise_front_end.team2.ui.theme.AppTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarScreen() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
            val activities = remember { mutableStateMapOf<LocalDate, String>() } // Stores activities for dates
            var showDialog by remember { mutableStateOf(false) } // State to control the dialog
            var selectedDate by remember { mutableStateOf<LocalDate?>(null) } // Track the selected date

            Column(
                modifier = Modifier.padding(16.dp),
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
                    onDayClick = { date ->
                        selectedDate = date
                        showDialog = true // Show the dialog when a day is clicked
                    }
                )
            }

            // Display the dialog if needed
            if (showDialog && selectedDate != null) {
                ShowAddActivityDialog(
                    selectedDate = selectedDate!!,
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
    activities: Map<LocalDate, String>,
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
                    activity = activities[date],
                    onClick = { onDayClick(date) }
                )
            }
        }
    }
}

@Composable
fun DayCell(date: LocalDate, activity: String?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .background(
                color = if (activity != null) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else Color.Transparent,
                shape = MaterialTheme.shapes.small
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = date.dayOfMonth.toString(),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
            activity?.let {
                Text(
                    text = "•",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ShowAddActivityDialog(
    selectedDate: LocalDate,
    activities: MutableMap<LocalDate, String>,
    onDismiss: () -> Unit
) {
    var activityText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Activity for $selectedDate") },
        text = {
            Column {
                Text("Enter activity:")
                TextField(
                    value = activityText,
                    onValueChange = { activityText = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (activityText.isNotBlank()) {
                    activities[selectedDate] = activityText
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
