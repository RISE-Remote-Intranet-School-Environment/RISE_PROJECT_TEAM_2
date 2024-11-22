package rise_front_end.team2.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import rise_front_end.team2.ui.screens.*
import rise_front_end.team2.ui.screens.detail.DetailScreen
import rise_front_end.team2.ui.screens.list.ListScreen
import rise_front_end.team2.ui.screens.screens_grades.GradeScreen
import rise_front_end.team2.ui.theme.AppTheme

@Composable
actual fun PlatformNavigation() {
    AppTheme {
        val navController: NavHostController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(
            bottomBar = {
                CommonNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { newRoute ->
                        if (newRoute == Screens.HomeScreen.route) {
                            // Navigate to HomeScreen and reset the back stack
                            navController.navigate(Screens.HomeScreen.route) {
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
                startDestination = Screens.HomeScreen.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                // Home Screen
                composable(route = Screens.HomeScreen.route) {
                    HomeScreen(
                        onSyllabusClick = {
                            navController.navigate(Screens.ListDestination.route)
                        },
                        onFileHostingClick = {
                            navController.navigate(Screens.FileHostingScreen.route)
                        }
                    )
                }

                // Main Navigation Screens
                composable(route = Screens.GradeScreen.route) {
                    GradeScreen()
                }

                composable(route = Screens.FavoriteScreen.route) {
                    FavoriteScreen()
                }

                composable(route = Screens.CalendarScreen.route) {
                    CalendarScreen()
                }

                composable(route = Screens.SyllabusScreen.route) {
                    SyllabusScreen()
                }

                composable(route = Screens.FileHostingScreen.route) {
                    FileHostingScreen()
                }

                // List Screen
                composable(route = Screens.ListDestination.route) {
                    ListScreen(
                        navigateToDetails = { objectId ->
                            navController.navigate("detail/$objectId")
                        }
                    )
                }

                // Detail Screen
                composable(
                    route = Screens.DetailDestination.route,
                    arguments = listOf(
                        navArgument("objectId") {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->
                    val objectId = backStackEntry.arguments?.getInt("objectId") ?: 0
                    DetailScreen(
                        objectId = objectId,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}