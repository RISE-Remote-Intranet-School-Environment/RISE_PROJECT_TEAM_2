package rise_front_end.team2.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
expect fun PlatformNavigation()

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
                    indicatorColor = MaterialTheme.colorScheme.secondary,// Couleur de l'indicateur de sélection (l'arrière-plan du bouton)
                )

            )
        }
    }
}