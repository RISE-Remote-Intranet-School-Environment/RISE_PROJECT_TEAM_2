package rise_front_end.team2.ui.screens.favorites

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
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.favorites.FavoritesObject
import rise_front_end.team2.data.favorites.FavoritesCourseObject
import rise_front_end.team2.data.favorites.FavoritesFileObject
import rise_front_end.team2.data.studentHelp.forum.Course
import rise_front_end.team2.data.studentHelp.forum.CourseFile
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.theme.AppTheme

@Composable
fun FavoritesScreen(
    navigateToCourse: (courseId: Int) -> Unit,
    navigateToFile: (courseId: Int, fileId: Int) -> Unit
) {
    AppTheme {
        val viewModel = koinViewModel<FavoritesViewModel>()
        val favorites by viewModel.favorites.collectAsState()

        AnimatedContent(favorites.isNotEmpty()) { favoritesAvailable ->
            if (favoritesAvailable) {
                FavouritesGrid(
                    favorites = favorites,
                    onCourseClick = { courseId -> navigateToCourse(courseId) },
                    onFileClick = { courseId, fileId -> navigateToFile(courseId, fileId) }
                )
            } else {
                EmptyScreenContent(Modifier.fillMaxSize())
            }
        }
    }
}

//@Composable
//private fun AddFavoriteButtons(viewModel: FavoritesViewModel) {
//    // Example course and file to add as favorites
//    val course = Course(courseID = 1000000, courseName = "Sample Course", teacherName = "John Doe", courseYear = "2024", forum = emptyList(), courseFiles = emptyList())
//    val courseFile = CourseFile(fileID = 400000, fileName = "Sample File", fileUrl = "https://example.com/file", messages = emptyList())
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        // Add Course Button
//        Button(onClick = { viewModel.addCourseToFavorites(course) }) {
//            Text("Add Course to Favorites")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Add File Button
//        Button(onClick = { viewModel.addFileToFavorites(course, courseFile) }) {
//            Text("Add File to Favorites")
//        }
//    }
//}

@Composable
private fun FavouritesGrid(
    favorites: List<FavoritesObject>,
    onCourseClick: (Int) -> Unit,
    onFileClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal).asPaddingValues()),
        contentPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical).asPaddingValues(),
    ) {
        items(
            favorites,
            key = { favorite ->
                when (favorite) {
                    is FavoritesCourseObject -> "course-${favorite.courseID}"
                    is FavoritesFileObject -> "file-${favorite.courseID}-${favorite.fileID}"
                }
            }
        ) { favorite ->
            FavoriteFrame(
                favorite = favorite,
                onCourseClick = onCourseClick,
                onFileClick = onFileClick
            )
        }
    }
}

@Composable
private fun FavoriteFrame(
    favorite: FavoritesObject,
    onCourseClick: (Int) -> Unit,
    onFileClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .padding(8.dp)
    ) {
        when (favorite) {
            is FavoritesCourseObject -> {
                Text(
                    text = "Course: ${favorite.courseName}",
                    style = MaterialTheme.typography.bodySmall
                )
                Button(onClick = { onCourseClick(favorite.courseID) }) {
                    Text("Go to Course")
                }
            }
            is FavoritesFileObject -> {
                Text(
                    text = "File: ${favorite.fileName} (Course: ${favorite.courseName})",
                    style = MaterialTheme.typography.bodySmall
                )
                Button(onClick = { onFileClick(favorite.courseID, favorite.fileID) }) {
                    Text("Go to File")
                }
            }
        }
    }
}