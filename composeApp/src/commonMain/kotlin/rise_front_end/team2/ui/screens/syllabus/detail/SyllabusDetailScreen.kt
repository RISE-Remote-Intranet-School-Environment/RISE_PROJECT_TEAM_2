package rise_front_end.team2.ui.screens.syllabus.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rise_front_end.team2.data.objects.SyllabusObject
import rise_front_end.team2.ui.screens.EmptyScreenContent
import rise_front_end.team2.ui.theme.AppTheme
import rise_front_end_team2.composeapp.generated.resources.Res
import rise_front_end_team2.composeapp.generated.resources.back
import rise_front_end_team2.composeapp.generated.resources.label_artist
import rise_front_end_team2.composeapp.generated.resources.label_credits
import rise_front_end_team2.composeapp.generated.resources.label_date
import rise_front_end_team2.composeapp.generated.resources.label_dimensions
import rise_front_end_team2.composeapp.generated.resources.label_repository
import rise_front_end_team2.composeapp.generated.resources.label_title
import rise_front_end_team2.composeapp.generated.resources.label_medium
import rise_front_end_team2.composeapp.generated.resources.label_department

@Composable
fun SyllabusDetailScreen(
    objectId: Int,
    navigateBack: () -> Unit,
) {
    AppTheme {
        val viewModel = koinViewModel<SyllabusDetailViewModel>()
        val obj by viewModel.getObject(objectId).collectAsState(initial = null)

        AnimatedContent(obj != null) { objectAvailable ->
            if (objectAvailable) {
                ObjectDetails(obj!!, onBackClick = navigateBack)
            } else {
                EmptyScreenContent(Modifier.fillMaxSize())
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ObjectDetails(
    obj: SyllabusObject,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = obj.title, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier.windowInsetsPadding(WindowInsets.systemBars)
    ) { paddingValues ->
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            KamelImage(
                resource = asyncPainterResource(data = obj.primaryImageSmall),
                contentDescription = obj.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            SelectionContainer {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        obj.title,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(Modifier.height(6.dp))
                    LabeledInfo(stringResource(Res.string.label_title), obj.title)
                    LabeledInfo(stringResource(Res.string.label_artist), obj.artistDisplayName)
                    LabeledInfo(stringResource(Res.string.label_date), obj.objectDate)
                    LabeledInfo(stringResource(Res.string.label_dimensions), obj.dimensions)
                    LabeledInfo(stringResource(Res.string.label_medium), obj.medium)
                    LabeledInfo(stringResource(Res.string.label_department), obj.department)
                    LabeledInfo(stringResource(Res.string.label_repository), obj.repository)
                    LabeledInfo(stringResource(Res.string.label_credits), obj.creditLine)
                }
            }
        }
    }
}

@Composable
private fun LabeledInfo(
    label: String,
    data: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(vertical = 4.dp)) {
        Spacer(Modifier.height(6.dp))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$label: ")
                }
                append(data)
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
