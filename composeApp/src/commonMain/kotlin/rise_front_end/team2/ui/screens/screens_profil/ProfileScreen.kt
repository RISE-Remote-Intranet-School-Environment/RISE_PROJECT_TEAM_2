package rise_front_end.team2.ui.screens.screens_profil

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.ui.theme.AppTheme
import androidx.compose.material3.*
import rise_front_end.team2.ui.screens.screens_grades.CircularButton

@Composable
fun ProfileScreen(onShopClick: () -> Unit,) {

    // Apply the theme
    AppTheme {
        val viewModel = koinViewModel<ProfileViewModel>()
        val objects by viewModel.objects.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ThemeToggleSwitch()
            // Profile content
            objects.forEach { profileObject ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Info", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(30.dp)
                        .animateContentSize()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 40.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Class",
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                profileObject.className,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Option",
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                profileObject.option,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Year",
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                profileObject.year.toString(),
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Store Point", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(8.dp)
                        .animateContentSize()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 40.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                profileObject.points.toString() + " points",
                                color = Color.White,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            CircularButton(onClick = onShopClick, Icons.Default.ShoppingBasket, MaterialTheme.colorScheme.primaryContainer, 45)

                        }
                    }
                }


            }
        }
    }
}

@Composable
fun CustomSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.secondaryContainer,
            uncheckedThumbColor = MaterialTheme.colorScheme.primaryContainer,
            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}


@Composable
fun ThemeToggleSwitch() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = if (AppTheme.isDarkMode) "Dark Theme" else "Light Theme")

        Spacer(modifier = Modifier.width(8.dp))

        CustomSwitch(
            isChecked = AppTheme.isDarkMode,
            onCheckedChange = { AppTheme.isDarkMode = it }
        )
    }
}
