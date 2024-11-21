package rise_front_end.team2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val currentYear = LocalDate.now().year
    var selectedYear by remember { mutableStateOf(currentYear) }
    val months = (1..12).map { YearMonth.of(selectedYear, it) }

    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            YearSelector(selectedYear) { selectedYear = it }
            Spacer(modifier = Modifier.height(8.dp))
            MonthGrid(months)
        }
    }
}

@Composable
fun YearSelector(currentYear: Int, onYearSelected: (Int) -> Unit) {
    val adjacentYears = (currentYear - 2)..(currentYear + 2)
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        items(adjacentYears.toList()) { year ->
            Text(
                text = year.toString(),
                fontSize = 20.sp,
                color = if (year == currentYear) Color.Blue else Color.Black,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onYearSelected(year) },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MonthGrid(months: List<YearMonth>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        months.forEach { month ->
            MonthView(month)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun MonthView(month: YearMonth) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = month.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(8.dp)
        )
        DaysGrid(month)
    }
}

@Composable
fun DaysGrid(month: YearMonth) {
    val daysInMonth = month.lengthOfMonth()
    val firstDayOfMonth = month.atDay(1).dayOfWeek.value % 7 // Décalage pour aligner le premier jour
    val dayNumbers = (1..daysInMonth).map { it.toString() }

    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { dayName ->
                Text(
                    text = dayName,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.width(32.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Spacer(modifier = Modifier.width((firstDayOfMonth * 32).dp)) // Décalage pour aligner le premier jour
            dayNumbers.forEach { day ->
                DayView(day)
            }
        }
    }
}

@Composable
fun DayView(day: String) {
    Text(
        text = day,
        fontSize = 16.sp,
        modifier = Modifier
            .padding(4.dp)
            .size(32.dp),
        textAlign = TextAlign.Center
    )
}
