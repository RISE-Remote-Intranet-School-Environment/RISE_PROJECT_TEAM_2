package rise_front_end.team2.ui.screens.list_syllabus

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import rise_front_end_team2.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import rise_front_end_team2.composeapp.generated.resources.no_data_available

@Composable
fun EmptyScreenContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(stringResource(Res.string.no_data_available))
    }
}
