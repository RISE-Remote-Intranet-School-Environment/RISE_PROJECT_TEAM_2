package rise_front_end.team2.ui.screens.StudentHelpForum.answer

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.studentHelp.forum.Answer
import rise_front_end.team2.data.studentHelp.forum.ForumMessage
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.theme.AppTheme

@Composable
fun ForumMessageAnswersScreen(
    courseId: Int,
    messageId: Int,
    navigateBack: () -> Unit,
) {
    AppTheme {
        val viewModel = koinViewModel<ForumMessageAnswersViewModel>()
        val message by viewModel.getForumMessage(courseId, messageId).collectAsState(initial = null)

        AnimatedContent(message != null) { messageAvailable ->
            if (messageAvailable) {
                AnswerList(
                    message = message!!,
                    courseId = courseId,
                    viewModel = viewModel,
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
private fun AnswerList(
    message: ForumMessage,
    courseId: Int, // Add courseId
    viewModel: ForumMessageAnswersViewModel, // Add ViewModel
    navigateBack: () -> Unit,
) {
    var showAddAnswerDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Discussion") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            AddAnswerButton(
                onClick = { showAddAnswerDialog = true }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            OriginalPostFrame(message)

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )

            Text(
                text = "Answers",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            message.answers.forEach { answer ->
                AnswerFrame(answer)
            }
        }


        if (showAddAnswerDialog) {
            AddAnswerDialog(
                onSubmit = { description ->
                    val newAnswer = Answer(
                        answerID = 0, // Will need to depend on the storage
                        content = description,
                        author = "CurrentUser", //Replace with actual username
                         timestamp = System.currentTimeMillis().toString(),
                        likes = 0
                    )
                    viewModel.addAnswer(courseId, message.messageID, newAnswer)
                    showAddAnswerDialog = false
                },
                onCancel = { showAddAnswerDialog = false }
            )
        }
    }
}

@Composable
private fun AnswerFrame(
    answer: Answer,
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
            text = answer.content,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "By: ${answer.author} | ${answer.timestamp} | Likes: ${answer.likes}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun OriginalPostFrame(
    message: ForumMessage,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = message.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message.content,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "By: ${message.author} | ${message.timestamp}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun AddAnswerButton(
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Answer"
        )
    }
}

@Composable
fun AddAnswerDialog(
    onSubmit: (description: String) -> Unit,
    onCancel: () -> Unit
) {
    var description by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(min = 400.dp, max = 500.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Add New Answer",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Write your answer here") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp, max = 300.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onCancel) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (description.isNotBlank()) {
                                onSubmit(description)
                            }
                        },
                        enabled = description.isNotBlank()
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}