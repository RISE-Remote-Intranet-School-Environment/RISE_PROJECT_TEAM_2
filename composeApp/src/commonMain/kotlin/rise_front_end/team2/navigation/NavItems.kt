package rise_front_end.team2.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

object NavigationItems {
    fun getNavItems() = listOf(
        NavItem(
            label = "Home",
            icon = Icons.Default.Home,
            route = Screens.HomeScreen.route
        ),
        NavItem(
            label = "Student Forum",
            icon = Icons.Default.Forum,
            route = Screens.StudentHelpForumList.route
        ),
        NavItem(
            label = "Favorites",
            icon = Icons.Default.Star,
            route = Screens.FavoritesScreen.route
        ),
        NavItem(
            label = "Calendar",
            icon = Icons.Default.CalendarMonth,
            route = Screens.CalendarScreen.route
        )
    )
}