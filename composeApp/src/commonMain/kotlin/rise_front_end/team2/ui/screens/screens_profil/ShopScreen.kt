package rise_front_end.team2.ui.screens.screens_profil

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.PestControl
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rise_front_end.team2.ui.theme.AppTheme
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight

import rise_front_end.team2.ui.theme.GradesGood
import rise_front_end.team2.ui.theme.GradesBad

@Composable
fun ShopScreen() {
    AppTheme {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),

        ) {
            Text(
                "Food/Drink",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
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
                        Column {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Coffee,
                                    contentDescription = null,
                                    tint = Color.White // Couleur personnalisée
                                )
                                Text(
                                    "Coffee",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )


                            }
                            Text(
                                "50 points",
                                color = GradesGood,
                                fontSize = 20.sp
                            )

                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.Default.ShoppingCartCheckout,
                            contentDescription = null,
                            tint = Color.White // Couleur personnalisée
                        )

                    }

                }
            }
            Spacer(modifier = Modifier.height(16.dp))
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
                        Column {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Fastfood,
                                    contentDescription = null,
                                    tint = Color.White // Couleur personnalisée
                                )
                                Text(
                                    "Sandwich",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )


                            }

                        Text(
                            "3000 points",
                            color = GradesBad,
                            fontSize = 20.sp
                        )

                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.Default.ShoppingCartCheckout,
                            contentDescription = null,
                            tint = Color.White // Couleur personnalisée
                        )

                    }

                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Custom App",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
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
                        Column {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.PestControl,
                                    contentDescription = null,
                                    tint = Color.White // Couleur personnalisée
                                )
                                Text(
                                    "Avatar",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )


                            }
                            Text(
                                "1200 points",
                                color = GradesBad,
                                fontSize = 20.sp
                            )

                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.Default.ShoppingCartCheckout,
                            contentDescription = null,
                            tint = Color.White // Couleur personnalisée
                        )

                    }

                }
            }
        }
    }
}
