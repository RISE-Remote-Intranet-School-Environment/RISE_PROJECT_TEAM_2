package rise_front_end.team2.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label:String,
    val icon: ImageVector,
    val route: String
)

object NavigationItems {
    fun getNavItems() = listOf(
    NavItem(
        label = "Home",
        icon = Icons.Default.Home,
        route = Screens.HomeScreen.name
    ),
    NavItem(
        label = "Grades",
        icon = Icons.Default.Person,
        route = Screens.GradeScreen.name
    ),
    NavItem(
        label = "Favorites",
        icon = Icons.Default.Star,
        route = Screens.FavoriteScreen.name
    ),
    NavItem(
        label = "Calendar",
        icon = Icons.Default.CalendarMonth,//Jsp pourquoi il reste en rouge alors que ça fonctionne
        route = Screens.CalendarScreen.name
    )
) }