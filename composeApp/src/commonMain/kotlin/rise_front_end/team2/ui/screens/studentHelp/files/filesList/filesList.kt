package rise.front_end.team2.ui.screens.studentHelp.files.filesList

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
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
import androidx.compose.ui.layout.ContentScale
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
    val screenHeight =
        with(LocalDensity.current) { LocalContext.current.resources.displayMetrics.heightPixels.toDp() }
    var isExpanded by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(courseFile.inFavorites) }
    var fileLikes by remember { mutableStateOf(courseFile.fileLikes) }

    // Find the most liked message
    val mostLikedMessage = courseFile.messages.maxByOrNull { it.likes ?: 0 }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header section with profile picture, title, and favorite icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Profile Picture
                AsyncImage(
                    model = courseFile.profilePicture,
                    contentDescription = "Profile picture of ${courseFile.fileAuthor}",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Title and Favorite Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Title and File Icon
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

                    // Favorite Icon
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            if (isFavorite) {
                                viewModel.addToFavorites(courseId, courseFile.fileID)
                            } else {
                                viewModel.removeFromFavorites(courseId, courseFile.fileID)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                }
            }


            // Author and file details
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "By: ${courseFile.fileAuthor}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Bottom row with Like and Chat icons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Like Button
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        viewModel.likeFile(courseId, courseFile.fileID)
                        fileLikes++
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Like",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Text(
                    text = fileLikes.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            // Chat/Discussions Button
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "Discussions",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Text(
                    text = courseFile.messages.size.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        // Expanded content
        if (isExpanded) {
            Spacer(modifier = Modifier.height(16.dp))

            // File details
            Text(
                text = "Uploaded on: ${courseFile.fileDate}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Most liked discussion in an inner card
            mostLikedMessage?.let { message ->
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Most Liked answer:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = message.profilePicture,
                                contentDescription = "Profile picture of ${message.author}",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "${message.author}: ${message.content}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ThumbUp,
                                        contentDescription = "Likes",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .padding(end = 4.dp)
                                    )
                                    Text(
                                        text = "${message.likes}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // File preview and action buttons
            Spacer(modifier = Modifier.height(16.dp))
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
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "Go to Discussion",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }

                IconButton(
                    onClick = { downloadFile(context, courseFile.fileUrl, courseFile.fileName) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer

                    )
                }
            }
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
