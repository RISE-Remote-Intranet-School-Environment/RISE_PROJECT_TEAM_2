package rise_front_end.team2.ui.screens.screens_grades
import android.content.Context
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.data_grades.GradesObject
import rise_front_end.team2.data.data_grades.downloadFile
import rise_front_end.team2.ui.screens.EmptyScreenContent


@Composable
fun GradeScreen(onRegistrationClick: () -> Unit,) {
    AppTheme {
        val viewModel = koinViewModel<GradeViewModel>()
        val objects by viewModel.objects.collectAsState()
        val context = LocalContext.current
        var searchText by remember { mutableStateOf("") }

        // Filtrer les objets en fonction du texte de recherche
        val filteredObjects = objects.filter {
            it.nameUe.contains(searchText, ignoreCase = true)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp)) // Espacement entre la TopBar et les autres composants

            AnimatedContent(objects.isNotEmpty()) { objectsAvailable ->
                if (objectsAvailable) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ButtonBar(
                            context = context,
                            searchText = searchText,
                            onSearchTextChange = { searchText = it },
                            onRegistrationClick = onRegistrationClick)
                        if (filteredObjects.isEmpty() && objects.isNotEmpty()) {
                            Text(
                                text = "Aucun résultat trouvé",
                                modifier = Modifier.fillMaxSize(),
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        } else {
                            ExpandableCreditContainer(objects = filteredObjects)
                            CourseSection(objects = filteredObjects)
                        }

                    }
                } else {
                    EmptyScreenContent(Modifier.fillMaxSize())
                }
            }


        }
    }
}




@Composable
fun ButtonBar(context: Context, searchText: String, onSearchTextChange: (String) -> Unit, onRegistrationClick: () -> Unit,) {
    var showSearchBar by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularButton(onClick = { downloadFile(context, "grades.json") }, Icons.Default.Download, MaterialTheme.colorScheme.primary, 45)
        Spacer(modifier = Modifier.width(16.dp))
        CircularButton(onClick = onRegistrationClick, Icons.Default.DriveFileRenameOutline, MaterialTheme.colorScheme.primary, 45)

        Spacer(modifier = Modifier.width(16.dp))
        CircularButton({showSearchBar = !showSearchBar}, Icons.Default.Search, MaterialTheme.colorScheme.primary, 45)
    }
    if (showSearchBar) {
        TextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            placeholder = {
                Text(
                    text = "Search...",
                    color = if (isFocused) Color.Black else Color.White // Couleur dynamique
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .onFocusChanged { focusState -> isFocused = focusState.isFocused }, // Met à jour l'état de focus
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
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
        CircularButton({}, Icons.Default.ArrowBackIosNew, MaterialTheme.colorScheme.secondary, 35)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Previous years",
            textDecoration = TextDecoration.Underline,
            color = Color.Gray,
            fontSize = 14.sp,
        )

    }
    Spacer(modifier = Modifier.height(8.dp))


}

@Composable
fun ExpandableCreditContainer(objects: List<GradesObject>,) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp))
            .padding(16.dp)
            .animateContentSize()
    ) {
        Column {
            val totalEcts = objects.sumOf { it.ects }
            val ectsNumber = objects.sumOf { gradesObject ->
                gradesObject.list.sumOf {
                    if(it.grades > 10){
                        it.ectsNumber
                    }
                    else{
                        0
                    }
                }
            }
            val moyenneGenerale = if (totalEcts > 0) {
                objects.sumOf { gradesObject ->
                    gradesObject.list.sumOf { it.grades * it.ectsNumber }
                } /totalEcts
            } else 0

            val icon = if (ectsNumber > 30) Icons.Default.CheckCircleOutline else Icons.Default.Cancel
            val text = if (ectsNumber > 30) "Accepted" else "Not Accepted"


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Credit amount",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp,
                    )

                    Text(
                        text = "$ectsNumber/$totalEcts",
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
                            text = "Grade point average",
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                        )
                        Text(
                            text = "$moyenneGenerale/20",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "Jury's decision",
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                        )
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(30.dp) // Taille de l'icône, indépendante de la Box
                            )
                            Text(
                                text = text,
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
fun CourseSection(objects: List<GradesObject>,) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) { objects.forEach { gradesObject ->
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = gradesObject.nameUe, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = (gradesObject.ects.toString() +" ECTS"), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        val list = gradesObject.list
        list.forEach { element ->
            val cardColor = if (element.grades > 10) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onError
            GradeCard(
                subject = element.name,
                grade = element.grades,
                maxGrade = 20,
                credits = element.ectsNumber,
                color = cardColor
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))


    }
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

