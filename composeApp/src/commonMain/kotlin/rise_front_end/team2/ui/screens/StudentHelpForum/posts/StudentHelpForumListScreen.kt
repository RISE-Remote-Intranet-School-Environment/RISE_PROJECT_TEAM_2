package rise_front_end.team2.ui.screens.StudentHelpForum.posts

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    val viewModel = koinViewModel<StudentHelpForumDetailViewModel>() // ViewModel instance

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
    Column(
        modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Text(
            text = message.content,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "By: ${message.author} | ${message.timestamp}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun AddForumMessageButton(
    courseId: Int,
    viewModel: StudentHelpForumDetailViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var messageContent by remember { mutableStateOf("") }

    Button(onClick = { showDialog = true }) {
        Text("Add Message")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add New Forum Message") },
            text = {
                TextField(
                    value = messageContent,
                    onValueChange = { messageContent = it },
                    label = { Text("Message Content") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newMessage = ForumMessage(
                            messageID = 0, // Will need to depend on the storage
                            content = messageContent,
                            author = "CurrentUser", // Need to replace with username
                            timestamp = System.currentTimeMillis().toString(), //Need to update it to display real time
                            answers = emptyList()
                        )
                        viewModel.addForumMessage(courseId, newMessage)
                        showDialog = false
                    }
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}