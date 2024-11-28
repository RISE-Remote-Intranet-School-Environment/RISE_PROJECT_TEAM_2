package rise_front_end.team2.ui.screens.StudentHelpForum.courseslist

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.studentHelp.forum.Course
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.theme.AppTheme

@Composable
fun StudentHelpForumListScreen(
    navigateToCourseDetails: (courseId: Int) -> Unit
) {
    AppTheme {
        val viewModel = koinViewModel<StudentHelpForumListViewModel>()
        val courses by viewModel.courses.collectAsState()

        AnimatedContent(courses.isNotEmpty()) { coursesAvailable ->
            if (coursesAvailable) {
                CourseGrid(
                    courses = courses,
                    onCourseClick = navigateToCourseDetails,
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
    onCourseClick: (Int) -> Unit,
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
                onClick = { onCourseClick(course.courseID) },
            )
        }
    }
}

@Composable
private fun CourseFrame(
    course: Course,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .padding(8.dp)
            .clickable { onClick() }
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
    }
}
