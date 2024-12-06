package rise_front_end.team2.ui.screens.StudentHelpForum.answer

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.studentHelp.forum.Answer
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
                AnswerList(message!!.answers, navigateBack)
            } else {
                EmptyScreenContent(Modifier.fillMaxSize())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnswerList(
    answers: List<Answer>,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Answers") },
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
            answers.forEach { answer ->
                AnswerFrame(answer)
            }
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
