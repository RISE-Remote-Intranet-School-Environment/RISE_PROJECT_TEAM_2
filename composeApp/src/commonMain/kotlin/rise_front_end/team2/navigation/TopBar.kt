package rise_front_end.team2.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.ui.screens.screens_grades.CircularButton
import rise_front_end.team2.ui.screens.screens_profil.ProfileViewModel
import rise_front_end.team2.ui.theme.Primary

@Composable
fun TopBar(name: String, onProfilClick: () -> Unit,) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val objects by viewModel.objects.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer, // Couleur personnalisée de la TopBar
                shape = RoundedCornerShape(
                    bottomStart = 50.dp,
                    bottomEnd = 50.dp
                )// Coins inférieurs arrondis
            )
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer, // Couleur personnalisée de la TopBar
                    shape = RoundedCornerShape(
                        bottomStart = 50.dp,
                        bottomEnd = 50.dp
                    ) // Coins inférieurs arrondis
                )
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 60.dp)) {
                Text(name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                if(name != "Profile") {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Master 2024-2025",
                        color = rise_front_end.team2.ui.theme.Secondary,
                        fontSize = 20.sp,

                    )
                }
                CircularButton(onClick = onProfilClick, Icons.Default.Person, MaterialTheme.colorScheme.primaryContainer, 45)
                if(name == "Profile") { objects.forEach { profileObject ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "User Name",
                            color = rise_front_end.team2.ui.theme.Secondary,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            profileObject.userName,
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
                            "Mail",
                            color = rise_front_end.team2.ui.theme.Secondary,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            profileObject.mail,
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
                            "Password",
                            color = rise_front_end.team2.ui.theme.Secondary,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            profileObject.password,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                }

            }
        }
    }
}
