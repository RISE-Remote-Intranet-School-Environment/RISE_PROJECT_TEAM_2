package rise_front_end.team2.ui.screens.StudentHelpForum.courseslist

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.studentHelp.forum.Course
import rise_front_end.team2.ui.screens.EmptyScreenContent
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
    Column(
        modifier
            .padding(8.dp)
    ) {
        Text(
            text = course.courseName,
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
            Button(onClick = onForumClick) {
                Text("Forum")
            }
            Button(onClick = onFilesClick) {
                Text("Files")
            }
        }
    }
}
