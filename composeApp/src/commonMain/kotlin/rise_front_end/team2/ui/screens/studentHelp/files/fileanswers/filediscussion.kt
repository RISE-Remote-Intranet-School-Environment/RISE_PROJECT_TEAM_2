package rise_front_end.team2.ui.screens.studentHelp.files.fileanswers

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.compose.viewmodel.koinViewModel
import rise.front_end.team2.ui.screens.studentHelp.files.filesList.getFileIcon
import rise_front_end.team2.data.studentHelp.forum.CourseFile
import rise_front_end.team2.data.studentHelp.forum.FileMessage
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.theme.AppTheme
import java.io.File
import java.net.URL

@Composable
fun FileDiscussionsScreen(
    courseId: Int,
    fileId: Int,
    navigateBack: () -> Unit,
) {
    AppTheme {
        val viewModel = koinViewModel<FileDiscussionsViewModel>()
        val file by viewModel.getFile(courseId, fileId).collectAsState(initial = null)

        AnimatedContent(file != null) { fileAvailable ->
            if (fileAvailable) {
                FileMessageList(
                    file = file!!,
                    courseId = courseId,
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
private fun FileMessageList(
    file: CourseFile,
    courseId: Int,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "File Discussions") },
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
            FilePreviewCard(
                file = file,
                courseId = courseId
            )

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )

            Text(
                text = "File Discussions",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            file.messages.forEach { message ->
                FileMessageFrame(message)
            }
        }
    }
}

@Composable
private fun FilePreviewCard(
    file: CourseFile,
    courseId: Int,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<FileDiscussionsViewModel>()
    var isFavorite by remember { mutableStateOf(file.inFavorites) }
    val context = LocalContext.current
    val screenHeight = with(LocalDensity.current) {
        context.resources.displayMetrics.heightPixels.toDp()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Top row with file details
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // File name and author
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = file.fileName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "By: ${file.fileAuthor}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Favorite and interaction stats
                Column(horizontalAlignment = Alignment.End) {
                    // Favorite toggle
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            if (isFavorite) {
                                viewModel.addToFavorites(courseId, file.fileID)
                            } else {
                                viewModel.removeFromFavorites(courseId, file.fileID)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
                        )
                    }

                    // Likes and comments
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Likes",
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${file.fileLikes}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Default.Message,
                            contentDescription = "Messages",
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${file.messages.size}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            // File type icon and date
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = getFileIcon(file.fileName),
                    contentDescription = "File type icon"
                )

                Text(
                    text = file.fileDate,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Inner card for file preview
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 3),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    if (file.fileName.endsWith(".pdf")) {
                        PdfPreview(fileUrl = file.fileUrl)
                    } else if (file.fileName.endsWith(".jpg") || file.fileName.endsWith(".png")) {
                        ImagePreview(fileUrl = file.fileUrl)
                    } else {
                        Text(
                            text = "Preview not available for this file type",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FileMessageFrame(
    message: FileMessage,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Text(
            text = message.content,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "By: ${message.author} | ${message.timestamp} | Likes: ${message.likes}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun PdfPreview(fileUrl: String) {
    val context = LocalContext.current
    var pdfFile by remember { mutableStateOf<File?>(null) }

    // Preload the PDF on initial render
    LaunchedEffect(fileUrl) {
        if (fileUrl.isNotBlank() && Uri.parse(fileUrl).scheme != null) {
            pdfFile = downloadPdf(context, fileUrl)
        } else {
            Log.e("PdfPreview", "Invalid file URL")
        }
    }

    pdfFile?.let { file ->
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PDFView(context, null).apply {
                    fromFile(file)
                        .defaultPage(0)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .scrollHandle(DefaultScrollHandle(context))
                        .load()
                }
            }
        )
    } ?: Text("Preview is loading", color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxSize())
}

@Composable
private fun ImagePreview(fileUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(fileUrl)
            .crossfade(true)
            .build(),
        contentDescription = "File preview",
        modifier = Modifier.fillMaxSize()
    )
}

// Utility function for downloading PDF (same as in FileList screen)
suspend fun downloadPdf(context: Context, url: String): File? = withContext(Dispatchers.IO) {
    try {
        val file = File(context.cacheDir, "tempfile.pdf")
        URL(url).openStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: Exception) {
        Log.e("DownloadPdf", "Error downloading PDF", e)
        null
    }
}