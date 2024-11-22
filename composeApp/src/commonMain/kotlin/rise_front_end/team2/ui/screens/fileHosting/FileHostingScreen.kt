package rise_front_end.team2.ui.screens.fileHosting

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import rise_front_end.team2.data.FileManager.FileObject
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.theme.AppTheme

@Composable
fun FileHostingScreen(
    navigateToDetails: (objectId: Int) -> Unit
) {
    AppTheme {
        val viewModel = koinViewModel<FileHostingViewModel>()
        val objects by viewModel.objects.collectAsState()

        AnimatedContent(objects.isNotEmpty()) { objectsAvailable ->
            if (objectsAvailable) {
                ObjectGrid(
                    objects = objects,
                    onObjectClick = navigateToDetails,
                )
            } else {
                EmptyScreenContent(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun ObjectGrid(
    objects: List<FileObject>,
    onObjectClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal).asPaddingValues()),
        contentPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical).asPaddingValues(),
    ) {
        items(objects, key = { it.objectID }) { obj ->
            ObjectFrame(
                obj = obj,
                onClick = { onObjectClick(obj.objectID) },
            )
        }
    }
}

@Composable
private fun ObjectFrame(
    obj: FileObject,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .padding(8.dp)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.surface) // Background color for better contrast
            .padding(8.dp) // Inner padding for a cleaner layout
    ) {
        KamelImage(
            resource = asyncPainterResource(data = obj.primaryImageSmall),
            contentDescription = obj.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.LightGray), // Placeholder background
            onLoading = { /* Add loading UI here */ },
            onFailure = { /* Add error UI here */ }
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = obj.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = obj.artistDisplayName,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = obj.objectDate,
            style = MaterialTheme.typography.bodySmall
        )

//        // Example for additional info like file size
//        Text(
//            text = "Size: ${obj.fileSize} MB", // Assuming `fileSize` is a property
//            style = MaterialTheme.typography.bodySmall,
//            color = Color.Gray
//        )
    }
}
