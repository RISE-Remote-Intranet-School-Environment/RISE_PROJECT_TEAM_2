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
import androidx.compose.material3.Icon
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


@Composable
fun ProfileScreen(){
    AppTheme {
        val viewModel = koinViewModel<ProfileViewModel>()
        val objects by viewModel.objects.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ){
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Info", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Box (
                modifier = Modifier
                    .background(
                        rise_front_end.team2.ui.theme.Primary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(30.dp)
                    .animateContentSize()
            ){
                Column(
                    modifier = Modifier.padding(horizontal = 40.dp)
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            "Class",
                            color = rise_front_end.team2.ui.theme.Secondary,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("class", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            "Option",
                            color = rise_front_end.team2.ui.theme.Secondary,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("option", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            "Year",
                            color = rise_front_end.team2.ui.theme.Secondary,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("2025", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Store Point", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        rise_front_end.team2.ui.theme.Primary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp)
                    .animateContentSize()
            ){
                Column(
                    modifier = Modifier.padding(horizontal = 40.dp)
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            "150 points",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.Default.ShoppingBasket,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(25.dp) // Taille de l'icône, indépendante de la Box
                        )
                    }

                }
            }
        }
    }
}

