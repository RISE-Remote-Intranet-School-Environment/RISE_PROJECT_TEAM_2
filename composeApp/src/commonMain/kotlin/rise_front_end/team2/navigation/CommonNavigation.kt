package rise_front_end.team2.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
                }
            )
        }
    }
}