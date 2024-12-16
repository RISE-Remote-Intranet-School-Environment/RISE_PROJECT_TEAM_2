package rise.front_end.team2.ui.screens.studentHelp.files.filesList

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.studentHelp.forum.CourseFile
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.screens.studentHelp.files.filesList.CourseFilesViewModel
import rise_front_end.team2.ui.theme.AppTheme
import java.io.File
import java.net.URL

@Composable
fun CourseFilesListScreen(
    courseId: Int,
    navigateToFileDiscussions: (courseId: Int, fileId: Int) -> Unit,
    navigateBack: () -> Unit,
) {
    AppTheme {
        val viewModel = koinViewModel<CourseFilesViewModel>()
        val course by viewModel.getCourse(courseId).collectAsState(initial = null)

        AnimatedContent(course != null) { courseAvailable ->
            if (courseAvailable) {
                FileList(
                    courseFiles = course!!.courseFiles,
                    courseId = courseId,
                    navigateToFileDiscussions = navigateToFileDiscussions,
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
private fun FileList(
    courseFiles: List<CourseFile>,
    courseId: Int,
    navigateToFileDiscussions: (courseId: Int, fileId: Int) -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Course Files") },
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
            courseFiles.forEach { file ->
                FileFrame(
                    courseFile = file,
                    courseId = courseId,
                    onClick = { navigateToFileDiscussions(courseId, file.fileID) }
                )
            }
        }
    }
}

@Composable
private fun FileFrame(
    courseFile: CourseFile,
    courseId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<CourseFilesViewModel>()
    val context = LocalContext.current
    val screenHeight = with(LocalDensity.current) { LocalContext.current.resources.displayMetrics.heightPixels.toDp() }
    var isExpanded by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(courseFile.inFavorites) }

    // Find the most liked message
    val mostLikedMessage = courseFile.messages.maxByOrNull { it.likes ?: 0 }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp)
    ) {
        // File icon and title row
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 40.dp) // Added space for the star icon in the top-right corner
        ) {
            // File icon and title row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Left side: Icon and filename
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = getFileIcon(courseFile.fileName),
                        contentDescription = "File type icon",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = courseFile.fileName,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Right side: Likes and message count (when not expanded)
                if (!isExpanded) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Likes",
                            modifier = Modifier.size(16.dp).padding(end = 4.dp)
                        )
                        Text(
                            text = "${courseFile.fileLikes}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Message,
                            contentDescription = "Messages",
                            modifier = Modifier.size(16.dp).padding(end = 4.dp)
                        )
                        Text(
                            text = "${courseFile.messages.size}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Expandable preview section
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))

                // File details when expanded
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    Column {
                        Text(
                            text = "Author: ${courseFile.fileAuthor}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Date: ${courseFile.fileDate}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Most liked message
                mostLikedMessage?.let { message ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Most Liked Discussion:",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${message.author}: ${message.content}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Likes: ${message.likes}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Preview section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight / 4)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(10.dp)
                ) {
                    if (courseFile.fileName.endsWith(".pdf")) {
                        PdfPreview(fileUrl = courseFile.fileUrl)
                    } else if (courseFile.fileName.endsWith(".jpg") || courseFile.fileName.endsWith(".png")) {
                        ImagePreview(fileUrl = courseFile.fileUrl)
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Go to Discussion", color = Color.White)
                    }

                    Button(
                        onClick = { downloadFile(context, courseFile.fileUrl, courseFile.fileName) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Download", color = Color.White)
                    }
                }
            }
        }

        // Star icon positioned at the top-right corner
        IconButton(
            onClick = {
                isFavorite = !isFavorite
                if (isFavorite) {
                    viewModel.addToFavorites(courseId, courseFile.fileID)
                } else {
                    viewModel.removeFromFavorites(courseId, courseFile.fileID)
                }
            },
            modifier = Modifier
                .padding(4.dp)  // This padding moves the icon to the top-right corner
                .align(Alignment.TopEnd)  // Places the icon in the top-right
                .size(24.dp)  // Adjust the icon size
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                tint = MaterialTheme.colorScheme.primaryContainer
            )
        }
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

//Not really used right now but will be later
//Might need to replace icons with material3. Right now it's material
fun getFileIcon(fileName: String): ImageVector {
    return when {
        fileName.endsWith(".pdf") -> Icons.Default.PictureAsPdf
        fileName.endsWith(".docx") -> Icons.Default.Description
        fileName.endsWith(".xlsx") -> Icons.Default.TableChart
        fileName.endsWith(".txt") -> Icons.AutoMirrored.Filled.TextSnippet
        else -> Icons.AutoMirrored.Filled.InsertDriveFile
    }
}

//Need to sorta download the pdf before previewing it. Need to check if it can be done more easily
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

fun downloadFile(context: Context, url: String, name: String) {
    try {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(name)
            .setDescription("Downloading file")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.substringAfterLast("/"))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = ContextCompat.getSystemService(context, DownloadManager::class.java)
        downloadManager?.enqueue(request)
    } catch (e: Exception) {
        Log.e("Download", "Error downloading file", e)
    }
}
