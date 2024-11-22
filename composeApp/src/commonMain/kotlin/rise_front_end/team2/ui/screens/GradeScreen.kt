package rise_front_end.team2.ui.screens
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rise_front_end.team2.ui.theme.AppTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextDecoration


@Composable
fun GradeScreen() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEFF4FF))
                .verticalScroll(rememberScrollState())
        ) {
            TopBar("Grades") // La TopBar prend maintenant toute la largeur sans être affectée par un padding
            Spacer(modifier = Modifier.height(16.dp)) // Espacement entre la TopBar et les autres composants
            Column(modifier = Modifier.padding(16.dp)) {
                ButtonBar()
                ExpandableCreditContainer()
                CourseSection()
            }
        }
    }
}

@Composable
fun TopBar(name: String) {
    Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = rise_front_end.team2.ui.theme.Secondary, // Couleur personnalisée de la TopBar
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
                    color = rise_front_end.team2.ui.theme.Primary, // Couleur personnalisée de la TopBar
                    shape = RoundedCornerShape(
                        bottomStart = 50.dp,
                        bottomEnd = 50.dp
                    ) // Coins inférieurs arrondis
                )
                .padding(vertical = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Master 2024-2025",
                    color = rise_front_end.team2.ui.theme.Secondary,
                    fontSize = 20.sp
                )
            }
        }
    }
}



@Composable
fun ButtonBar() {
    var showSearchBar by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularButton({}, Icons.Default.Download, rise_front_end.team2.ui.theme.Primary, 45)
        Spacer(modifier = Modifier.width(16.dp))
        CircularButton({}, Icons.Default.DriveFileRenameOutline, rise_front_end.team2.ui.theme.Primary, 45)
        Spacer(modifier = Modifier.width(16.dp))
        CircularButton({showSearchBar = !showSearchBar}, Icons.Default.Search, rise_front_end.team2.ui.theme.Primary, 45)
    }
    if (showSearchBar) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = {
                Text(
                    text = "Rechercher...",
                    color = if (isFocused) Color.Black else Color.White // Couleur dynamique
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .onFocusChanged { focusState -> isFocused = focusState.isFocused }, // Met à jour l'état de focus
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = rise_front_end.team2.ui.theme.Primary,
                focusedIndicatorColor = Color.White,
                cursorColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        )

    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularButton({}, Icons.Default.ArrowBackIosNew, rise_front_end.team2.ui.theme.Secondary, 35)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Années Précédentes",
            textDecoration = TextDecoration.Underline,
            color = Color.Gray,
            fontSize = 14.sp,
        )

    }
    Spacer(modifier = Modifier.height(8.dp))


}

@Composable
fun ExpandableCreditContainer() {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(rise_front_end.team2.ui.theme.Primary, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
            .animateContentSize()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Nombres de crédit",
                        color = rise_front_end.team2.ui.theme.Secondary,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "40/60",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.Remove else Icons.Default.Add,
                    contentDescription = if (isExpanded) "Réduire" else "Agrandir",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { isExpanded = !isExpanded },
                    tint = Color.White
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                // Contenu additionnel à afficher lorsqu'il est déplié
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "Moyenne Générale",
                            color = rise_front_end.team2.ui.theme.Secondary,
                            fontSize = 14.sp,
                        )
                        Text(
                            text = "14/20",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "Décision du jury",
                            color = rise_front_end.team2.ui.theme.Secondary,
                            fontSize = 14.sp,
                        )
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                imageVector = Icons.Default.CheckCircleOutline,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(30.dp) // Taille de l'icône, indépendante de la Box
                            )
                            Text(
                                text = "Admis",
                                color = Color.White,
                                fontSize = 12.sp,
                            )

                        }
                    }

                }
            }
        }
    }
}

@Composable
fun CourseSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) { Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Gestion 1",   fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "6 ECTS", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        GradeCard(subject = "Sciences Humaines", grade = 4, maxGrade = 20, credits = 3, color = rise_front_end.team2.ui.theme.Neutral)
        Spacer(modifier = Modifier.height(8.dp))
        GradeCard(subject = "Ethics Lab", grade = 18, maxGrade = 20, credits = 1, color = rise_front_end.team2.ui.theme.Tertiary)
        Spacer(modifier = Modifier.height(8.dp))
        GradeCard(subject = "Projets Qualités", grade = 4, maxGrade = 20, credits = 2, color = rise_front_end.team2.ui.theme.Neutral)
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Architecture and\n software quality",   fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "4 ECTS", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        GradeCard(subject = "Software Architecture", grade = 4, maxGrade = 20, credits = 2, color = rise_front_end.team2.ui.theme.Neutral)
        Spacer(modifier = Modifier.height(8.dp))
        GradeCard(subject = "Software Architecture and Quality Lab", grade = 18, maxGrade = 20, credits = 2, color = rise_front_end.team2.ui.theme.Tertiary)
    }
}

@Composable
fun GradeCard(subject: String, grade: Int, maxGrade: Int, credits: Int, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color, shape = RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Text(text = subject, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "$grade/$maxGrade", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "$credits ECTS", fontSize = 16.sp)
            }
        }
    }
}


@Composable
fun CircularButton(onClick: () -> Unit, icon: ImageVector, color: Color, size: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size.dp) // Taille du bouton (Box)
            .background(color, shape = CircleShape)
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(25.dp) // Taille de l'icône, indépendante de la Box
        )
    }
}

