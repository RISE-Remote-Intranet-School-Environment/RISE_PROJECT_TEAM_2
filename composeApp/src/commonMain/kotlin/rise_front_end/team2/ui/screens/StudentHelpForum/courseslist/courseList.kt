package rise_front_end.team2.ui.screens.StudentHelpForum.courseslist

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.studentHelp.forum.Course
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.screens.studentHelp.files.filesList.CourseFilesViewModel
import rise_front_end.team2.ui.theme.AppTheme

@Composable
fun StudentHelpCourseListScreen(
    navigateToForum: (courseId: Int) -> Unit,
    navigateToFiles: (courseId: Int) -> Unit
) {
    AppTheme {
        val viewModel = koinViewModel<StudentHelpForumListViewModel>()
        val courses by viewModel.courses.collectAsState()

        AnimatedContent(courses.isNotEmpty()) { coursesAvailable ->
            if (coursesAvailable) {
                CourseGrid(
                    courses = courses,
                    onForumClick = navigateToForum,
                    onFilesClick = navigateToFiles,
                )
            } else {
                EmptyScreenContent(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun CourseGrid(
    courses: List<Course>,
    onForumClick: (Int) -> Unit,
    onFilesClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal).asPaddingValues()),
        contentPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical).asPaddingValues(),
    ) {
        items(courses, key = { it.courseID }) { course ->
            CourseFrame(
                course = course,
                onForumClick = { onForumClick(course.courseID) },
                onFilesClick = { onFilesClick(course.courseID) },
            )
        }
    }
}

@Composable
private fun CourseFrame(
    course: Course,
    onForumClick: () -> Unit,
    onFilesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<StudentHelpForumListViewModel>()
    var isFavorite by remember { mutableStateOf(course.inFavorites) }

    Box(
        modifier = modifier
            .padding(8.dp)
            .width(150.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(
                text = course.courseName,
                modifier = Modifier.width(136.dp),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = course.teacherName,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = course.courseYear,
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    onClick = onForumClick) {
                    Text("Forum", color = Color.White)
                }
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    onClick = onFilesClick) {
                    Text("Files", color = Color.White)
                }
            }
        }

        IconButton(
            onClick = {
                isFavorite = !isFavorite
                if (isFavorite) {
                    viewModel.addToFavorites(course.courseID)
                } else {
                    viewModel.removeFromFavorites(course.courseID)
                }
            },
            modifier = Modifier
                .padding(start = 142.dp, top = 4.dp)
                .size(24.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                tint = MaterialTheme.colorScheme.primaryContainer
            )
        }
    }
}
