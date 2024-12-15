package rise_front_end.team2.ui.screens.favorites

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
            .fillMaxWidth()
            .background(
                color = AppTheme.colors.surfaceVariant,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        when (favorite) {
            is FavoritesCourseObject -> {
                Text(
                    text = "Course: ${favorite.courseName}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Button(
                    onClick = { onCourseClick(favorite.courseID) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Go to Course",
                        color = AppTheme.colors.onPrimary)
                }
            }
            is FavoritesFileObject -> {
                Text(
                    text = "${favorite.fileName} (Course: ${favorite.courseName})",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Description, // Use a file-related icon
                        contentDescription = "File icon",
                        modifier = Modifier.size(40.dp).padding(end = 8.dp)
                    )
                    Button(
                        onClick = { onFileClick(favorite.courseID, favorite.fileID) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Go to File",
                            color = AppTheme.colors.onPrimary,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}