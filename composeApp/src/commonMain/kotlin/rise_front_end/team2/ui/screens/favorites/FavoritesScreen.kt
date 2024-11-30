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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.favorites.FavoritesObject
import rise_front_end.team2.data.studentHelp.forum.Course
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.theme.AppTheme

@Composable
fun FavoritesScreen(
    navigateToCourse: (courseId: Int) -> Unit,
    navigateToFile: (fileId: Int) -> Unit
) {
    AppTheme {
        val viewModel = koinViewModel<FavoritesViewModel>()
        val favorites by viewModel.favorites.collectAsState()

        AnimatedContent(favorites.isNotEmpty()) { favoritesAvailable ->
            if (favoritesAvailable) {
                FavouritesGrid(
                    favorites = favorites,
                    onLinkClick = { linkID ->
                        // Check prefix of linkID and navigate accordingly
                        when {
                            linkID.toString().startsWith("1") -> navigateToCourse(linkID)
                            linkID.toString().startsWith("4") -> navigateToFile(linkID)
                        }
                    }
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
    onLinkClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal).asPaddingValues()),
        contentPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical).asPaddingValues(),
    ) {
        items(favorites, key = { it.linkID }) { favorite ->
            FavoriteFrame(
                favorite = favorite,
                onLinkClick = { onLinkClick(favorite.linkID) }
            )
        }
    }
}

@Composable
private fun FavoriteFrame(
    favorite: FavoritesObject,
    onLinkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .padding(8.dp)
    ) {
        Text(
            text = favorite.linkID.toString(),
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onLinkClick) {
                Text("Go to Link")
            }
        }
    }
}