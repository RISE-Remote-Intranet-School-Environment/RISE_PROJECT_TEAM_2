package rise_front_end.team2.ui.screens.studentHelp.files.filesList

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.studentHelp.forum.File
import rise_front_end.team2.data.studentHelp.forum.ForumMessage
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.theme.AppTheme

@Composable
fun CourseFilesScreen(
    courseId: Int,
    navigateToFileDiscussions: (courseId: Int, fileId: Int) -> Unit,
    navigateBack: () -> Unit,
) {
    AppTheme {
        val viewModel = koinViewModel<CourseFilesViewModel>()
        val course by viewModel.getCourse(courseId).collectAsState(initial = null)

        AnimatedContent(course != null) { courseAvailable ->
            if (courseAvailable) {
                FileList(
                    files = course!!.files,
                    courseId = courseId,
                    navigateToFileDiscussions = navigateToFileDiscussions,
                    navigateBack = navigateBack
                )
            } else {
                EmptyScreenContent(Modifier.fillMaxSize())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FileList(
    files: List<File>,
    courseId: Int,
    navigateToFileDiscussions: (courseId: Int, fileId: Int) -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Course Files") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            files.forEach { file ->
                FileFrame(
                    file = file,
                    onClick = { navigateToFileDiscussions(courseId, file.fileID) }
                )
            }
        }
    }
}

@Composable
private fun FileFrame(
    file: File,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Text(
            text = file.fileName,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "File URL: ${file.fileUrl}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
