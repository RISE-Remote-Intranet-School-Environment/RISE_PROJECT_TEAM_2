package rise_front_end.team2.ui.screens.StudentHelpForum.courseslist

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.studentHelp.forum.Course
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.theme.AppTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.materialkolor.ktx.harmonize
import rise_front_end.team2.ui.theme.Secondary

@Composable
fun StudentHelpCourseListScreen(
    navigateToForum: (courseId: Int) -> Unit,
    navigateToFiles: (courseId: Int) -> Unit
) {
    AppTheme {
        val viewModel = koinViewModel<StudentHelpForumListViewModel>()
        val courses by viewModel.courses.collectAsState()

        // Sort courses: Favorites first
        val sortedCourses = courses.sortedByDescending { it.inFavorites }

        AnimatedContent(sortedCourses.isNotEmpty()) { coursesAvailable ->
            if (coursesAvailable) {
                CourseGrid(
                    courses = sortedCourses,
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
        contentPadding = PaddingValues(16.dp),
    ) {
        items(courses, key = { it.courseID }) { course ->
            CourseCard(
                course = course,
                onForumClick = { onForumClick(course.courseID) },
                onFilesClick = { onFilesClick(course.courseID) },
            )
        }
    }
}



@Composable
private fun CourseCard(
    course: Course,
    onForumClick: () -> Unit,
    onFilesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFavorite by remember { mutableStateOf(course.inFavorites) }
    val viewModel = koinViewModel<StudentHelpForumListViewModel>()

    Card(
        colors = CardDefaults.cardColors(containerColor = colorScheme.secondaryContainer),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Course Name
                    Text(
                        text = course.courseName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .fillMaxWidth()
                    )
                }

                // Teacher Name and Year
                Column {
                    Text(
                        text = course.teacherName,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSecondaryContainer,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = course.courseYear,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSecondaryContainer,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                /*
                //If we want to add icons to the courses. But it looks a bit bad right now.
                //To uncomment if we want to add them
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Course Icon
                    val iconId = course.icon?.let { iconName ->
                        // Dynamically find the icon resource by its name

                        val context = LocalContext.current
                        val resId = context.resources.getIdentifier(
                            iconName, "drawable", context.packageName
                        )
                        if (resId != 0) resId else null

                    }

                    if (iconId != null) {
                        Icon(
                            painter = painterResource(id = iconId),
                            contentDescription = "${course.courseName} Icon",
                            modifier = Modifier.size(32.dp),
                            tint = colorScheme.primary
                        )
                    }
                }

                 */


                // Row for Forum and Files Icons
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onForumClick) {
                        Icon(
                            imageVector = Icons.Filled.Forum,
                            contentDescription = "Go to Forum",
                            tint = colorScheme.primaryContainer
                        )
                    }
                    IconButton(onClick = onFilesClick) {
                        Icon(
                            imageVector = Icons.Filled.Folder,
                            contentDescription = "Go to Files",
                            tint = colorScheme.primaryContainer
                        )
                    }
                }
            }

            // Favorite Button
            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    if (isFavorite) {
                        viewModel.addToFavorites(course.courseID)
                    } else {
                        viewModel.removeFromFavorites(course.courseID)
                    }
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
