package rise_front_end.team2.ui.screens.StudentHelpForum.posts

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
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
        LaunchedEffect(courseId) {
            viewModel.loadTagsForCourse(courseId)
        }
        val course by viewModel.getCourse(courseId).collectAsState(initial = null)

        AnimatedContent(course != null) { courseAvailable ->
            if (courseAvailable) {
                ForumMessageList(
                    messages = course!!.forum,
                    courseId = courseId,
                    navigateToAnswers = navigateToAnswers,
                    navigateBack = navigateBack,
                    viewModel = viewModel
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
    viewModel: StudentHelpForumDetailViewModel
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = " Forum Board") },
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
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Profile Picture
            AsyncImage(
                model = message.profilePicture,
                contentDescription = "Profile Picture of ${message.author}",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(2.dp, colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Message Info
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = message.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "By: ${message.author} | ${message.timestamp}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Display tags
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    message.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .background(colorScheme.primary.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall,
                                color = colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AddForumMessageButton(
    courseId: Int,
    viewModel: StudentHelpForumDetailViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    FloatingActionButton(onClick = { showDialog = true }) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Post a question"
        )
    }

    if (showDialog) {
        AddPostDialog(
            viewModel = viewModel,
            onSubmit = { title, description, selectedTags ->
                val newMessage = ForumMessage(
                    messageID = 0, // This ID would depend on the backend/database logic. Check addForumMessage for more infos
                    title = title,
                    content = description,
                    author = "CurrentUser", // Need to Replace this with actual username logic
                    timestamp = System.currentTimeMillis().toString(),
                    answers = emptyList(),
                    tags = selectedTags,
                    profilePicture = "https://i.imgur.com/0fvzn7p.png"
                )
                viewModel.addForumMessage(courseId, newMessage)
                showDialog = false
            },
            onCancel = { showDialog = false }
        )
    }
}

@Composable
fun AddPostDialog(
    viewModel: StudentHelpForumDetailViewModel,
    onSubmit: (title: String, description: String, selectedTags: List<String>) -> Unit,
    onCancel: () -> Unit
) {
    val tags by viewModel.tags.collectAsState()

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(min = 500.dp, max = 600.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Add New Forum Message",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                StyledAddPostForm(
                    tags = tags,
                    onSubmit = onSubmit,
                    onCancel = onCancel
                )
            }
        }
    }
}

@Composable
fun StyledAddPostForm(
    tags: List<String>,
    onSubmit: (title: String, description: String, selectedTags: List<String>) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val selectedTags by remember { mutableStateOf(mutableStateListOf<String>()) }
    var showValidationError by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Title",
            style = MaterialTheme.typography.bodyMedium,
        )
        TextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("Title of the post") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Description",
            style = MaterialTheme.typography.bodyMedium,
        )
        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Description for the forum post") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tags",
            style = MaterialTheme.typography.bodyMedium,
        )
        LazyColumn(
            modifier = Modifier.height(150.dp)
        ) {
            items(tags) { tag ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (selectedTags.contains(tag)) {
                                selectedTags.remove(tag)
                            } else {
                                selectedTags.add(tag)
                            }
                        }
                        .padding(8.dp)
                        .background(
                            if (selectedTags.contains(tag)) colorScheme.primary.copy(alpha = 0.2f)
                            else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedTags.contains(tag),
                        onCheckedChange = {
                            if (it) selectedTags.add(tag) else selectedTags.remove(tag)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tag,
                        color = if (selectedTags.contains(tag)) colorScheme.primary else Color.Unspecified
                    )
                }
            }
        }

        if (showValidationError) {
            Text(
                text = "Please select at least one tag.",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onCancel) { Text("Cancel") }
            Button(onClick = {
                if (selectedTags.isEmpty()) {
                    showValidationError = true
                } else {
                    showValidationError = false
                    onSubmit(title, description, selectedTags.toList())
                }
            }) { Text("Submit") }
        }
    }
}

