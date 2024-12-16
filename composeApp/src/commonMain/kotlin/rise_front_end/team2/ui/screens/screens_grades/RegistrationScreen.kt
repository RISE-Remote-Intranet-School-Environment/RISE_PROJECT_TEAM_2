package rise_front_end.team2.ui.screens.screens_grades

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.data_grades.GradesObject
import rise_front_end.team2.ui.theme.AppTheme


@Composable
fun RegistrationScreen() {
    AppTheme {
        val viewModel = koinViewModel<GradeViewModel>()
        val objects by viewModel.objects.collectAsState()

        // Centralisation de l'état des checkboxes
        val checkedStates = remember { mutableStateMapOf<String, Boolean>() }
        var isChecked by remember { mutableStateOf(false) }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "I wish to register to :",fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        objects.forEach { gradesObject ->
                            gradesObject.list.forEach { element ->
                                checkedStates[element.name] = it
                            }
                        }
                         },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.secondaryContainer,
                        uncheckedColor = MaterialTheme.colorScheme.primaryContainer,
                        checkmarkColor = Color.White
                    ),
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.Transparent, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "All exams",
                )
            }

            CheckButton(objects = objects, checkedStates = checkedStates)
        }
    }
}

@Composable
fun CheckButton(objects: List<GradesObject>, checkedStates: MutableMap<String, Boolean>) {
    Column(modifier = Modifier.padding(16.dp)) {
        objects.forEach { gradesObject ->
            Text(text = gradesObject.nameUe, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                gradesObject.list.forEach { element ->
                    val isChecked = checkedStates[element.name] ?: false

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(16.dp)
                            .animateContentSize()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { checkedStates[element.name] = it },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = MaterialTheme.colorScheme.secondaryContainer,
                                            uncheckedColor = Color.Gray,
                                            checkmarkColor = Color.White
                                         ),
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(Color.Transparent, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = element.name,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isChecked) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Inscription enregistrée!",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}



