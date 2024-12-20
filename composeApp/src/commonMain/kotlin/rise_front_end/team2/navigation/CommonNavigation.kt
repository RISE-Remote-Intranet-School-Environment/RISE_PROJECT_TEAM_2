package rise_front_end.team2.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.materialkolor.ktx.darken
import com.materialkolor.ktx.harmonize
import rise_front_end.team2.ui.theme.AppTheme
import rise_front_end.team2.ui.theme.Secondary


@Composable
expect fun PlatformNavigation()

@Preview
@Composable
fun PreviewCommonNavigationBar() {
    AppTheme {
        CommonNavigationBar(
            currentRoute = NavigationItems.getNavItems().first().route, // Set the first item as selected for the preview
            onNavigate = {} // No-op for preview
        )
    }
}

@Composable
fun CommonNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationItems.getNavItems().forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = { onNavigate(navItem.route) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = navItem.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,// Couleur de l'indicateur de sélection (l'arrière-plan du bouton)
                    selectedIconColor = MaterialTheme.colorScheme.primaryContainer ,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,

                )
            )
        }
    }
}