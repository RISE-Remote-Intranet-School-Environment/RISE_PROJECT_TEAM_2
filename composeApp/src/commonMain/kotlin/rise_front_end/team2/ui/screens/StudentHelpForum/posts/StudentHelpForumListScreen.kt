package rise_front_end.team2.ui.screens.StudentHelpForum.posts

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.studentHelp.forum.ForumMessage
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.theme.AppTheme

@Composable
fun StudentHelpForumPostsScreen(
    courseId: Int,
    navigateToAnswers: (courseId: Int, messageId: Int) -> Unit,
    navigateBack: () -> Unit,
) {
    AppTheme {
        val viewModel = koinViewModel<StudentHelpForumDetailViewModel>()
        val course by viewModel.getCourse(courseId).collectAsState(initial = null)

        AnimatedContent(course != null) { courseAvailable ->
            if (courseAvailable) {
                ForumMessageList(
                    messages = course!!.forum,
                    courseId = courseId,
                    navigateToAnswers = navigateToAnswers,
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
private fun ForumMessageList(
    messages: List<ForumMessage>,
    courseId: Int,
    navigateToAnswers: (courseId: Int, messageId: Int) -> Unit,
    navigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<StudentHelpForumDetailViewModel>()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Forum Messages") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            AddForumMessageButton(
                courseId = courseId,
                viewModel = viewModel
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp)
        ) {
            messages.forEach { message ->
                MessageFrame(
                    message = message,
                    onClick = { navigateToAnswers(courseId, message.messageID) }
                )
            }
        }
    }
}

@Composable
private fun MessageFrame(
    message: ForumMessage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = message.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "By: ${message.author} | ${message.timestamp}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun AddForumMessageButton(

    courseId: Int,
    viewModel: StudentHelpForumDetailViewModel
) { AppTheme{
    var showDialog by remember { mutableStateOf(false) }

    FloatingActionButton(onClick = { showDialog = true }) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add forum post"
        )
    }

    if (showDialog) {
        AddPostDialog(
            onSubmit = { title, description ->
                val newMessage = ForumMessage(
                    messageID = 0, // Will need to depend on the storage
                    title = title,
                    content = description,
                    author = "CurrentUser", // Replace with actual username
                    timestamp = System.currentTimeMillis().toString(), // Replace with proper timestamp
                    answers = emptyList()
                )
                viewModel.addForumMessage(courseId, newMessage)
                showDialog = false
            },
            onCancel = { showDialog = false }
        )
    }
}}

@Composable
fun AddPostDialog(
    onSubmit: (title: String, description: String) -> Unit,
    onCancel: () -> Unit
) { AppTheme{
    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(usePlatformDefaultWidth = false) // Important for custom width
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f) // 90% of screen width
                .heightIn(min = 500.dp, max = 600.dp), // Controlled height
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Add New Forum Message",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                StyledAddPostForm(
                    onSubmit = onSubmit,
                    onCancel = onCancel
                )
            }
        }
    }}
}

@Composable
fun StyledAddPostForm(
    onSubmit: (title: String, description: String) -> Unit,
    onCancel: () -> Unit
) { AppTheme{
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var fileSelected by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Title",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("Short and descriptive") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Description",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Details, actions taken, etc.") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = { fileSelected = !fileSelected }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Attach File",
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (fileSelected) "Not yet implemented" else "Attach File", color = MaterialTheme.colorScheme.primaryContainer)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                onClick = onCancel,
            ) {
                Text("Cancel", color = Color.White)
            }

            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        onSubmit(title, description)
                    }
                },
                enabled = title.isNotBlank() && description.isNotBlank()
            ) {
                Text("Submit")
            }
        }
    }}
}