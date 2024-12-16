package rise_front_end.team2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun HomeScreen(
    onSyllabusClick: () -> Unit,
    onFileHostingClick: () -> Unit,
    onStudentHelpForumClick: () -> Unit,
    onOutlookClick: () -> Unit = {} // Valeur par défaut pour éviter les erreurs
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Logo ECAM
        AsyncImage(
            model = "https://th.bing.com/th/id/OIP.MQVDBjtT_jShbrFSGyIjMwAAAA?rs=1&pid=ImgDetMain",
            contentDescription = "Logo ECAM",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Fit
        )

        // Boutons organisés en grille
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButtonWithText("Syllabus", Icons.Filled.MenuBook, onSyllabusClick)
                IconButtonWithText("Student Help", Icons.Filled.Help, onStudentHelpForumClick)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButtonWithText("File Sharing", Icons.Filled.Folder, onFileHostingClick)
                IconButtonWithText("Outlook", Icons.Filled.Email, onOutlookClick)
            }
        }
    }
}

@Composable
fun IconButtonWithText(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(width = 150.dp, height = 100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F2C4C))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = text, fontSize = 14.sp, color = Color.White)
        }
    }
}
