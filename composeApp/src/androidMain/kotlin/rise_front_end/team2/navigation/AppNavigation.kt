// AndroidMain/AppNavigation.kt
package rise_front_end.team2.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import rise_front_end.team2.ui.screens.*
import rise_front_end.team2.ui.theme.AppTheme

@Composable
actual fun PlatformNavigation() {
    AppTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(
            bottomBar = {
                CommonNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { newRoute ->
                        if (newRoute == Screens.HomeScreen.name) {
                            // Navigate to HomeScreen and reset the back stack
                            navController.navigate(Screens.HomeScreen.name) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        } else {
                            // Regular navigation for other screens
                            navController.navigate(newRoute) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screens.HomeScreen.name,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(route = Screens.HomeScreen.name) {
                    HomeScreen(
                        onSyllabusClick = {
                            navController.navigate(Screens.SyllabusScreen.name)
                        },
                        onFileHostingClick = {
                            navController.navigate(Screens.FileHostingScreen.name)
                        }
                    )
                }
                composable(route = Screens.GradeScreen.name) {
                    GradeScreen()
                }
                composable(route = Screens.FavoriteScreen.name) {
                    FavoriteScreen()
                }
                composable(route = Screens.CalendarScreen.name) {
                    CalendarScreen()
                }
                composable(route = Screens.SyllabusScreen.name) {
                    SyllabusScreen()
                }
                composable(route = Screens.FileHostingScreen.name) {
                    FileHostingScreen()
                }
            }
        }
    }
}
