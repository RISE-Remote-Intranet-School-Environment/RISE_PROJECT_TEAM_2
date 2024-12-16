package rise_front_end.team2.ui.screens.favorites

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.favorites.FavoritesObject
import rise_front_end.team2.data.favorites.FavoritesCourseObject
import rise_front_end.team2.data.favorites.FavoritesFileObject
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.theme.AppTheme
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, name = "Favorites Screen Preview")
@Composable
fun PreviewFavoritesScreen() {
    val mockFavorites = listOf(
        FavoritesFileObject(
            courseID = 1,
            courseName = "Introduction to Kotlin",
            fileID = 101,
            fileName = "Lecture Notes"
        ),
        FavoritesFileObject(
            courseID = 2,
            courseName = "Advanced Compose",
            fileID = 201,
            fileName = "Project Guidelines"
        ),
        FavoritesFileObject(
            courseID = 3,
            courseName = "Test of UI",
            fileID = 301,
            fileName = "Is Dark Mode OK?"
        ),
    )

    AppTheme {
        FavoritesList(
            favorites = mockFavorites,
            onCourseClick = { courseId -> /* Mock Navigation */ },
            onFileClick = { courseId, fileId -> /* Mock Navigation */ }
        )
    }
}

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
                FavoritesList(
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
private fun FavoritesList(
    favorites: List<FavoritesObject>,
    onCourseClick: (Int) -> Unit,
    onFileClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal).asPaddingValues()),
        contentPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical).asPaddingValues(),
    ) {
        items(
            items = favorites,
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
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        when (favorite) {
            is FavoritesCourseObject -> {
                Text(
                    text = "Course: ${favorite.courseName}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                FilledTonalButton(
                    onClick = { onCourseClick(favorite.courseID) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Go to Course"
                    )
                }
            }

            is FavoritesFileObject -> {
                // Text section at the top
                Text(
                    text = "${favorite.fileName} (Course: ${favorite.courseName})",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )

                // Bottom section with icon and button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "File icon",
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Bottom)
                    )

                    FilledTonalButton(
                        onClick = { onFileClick(favorite.courseID, favorite.fileID) },
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .height(46.dp),

                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                            disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text(
                            text = "Go to File",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}