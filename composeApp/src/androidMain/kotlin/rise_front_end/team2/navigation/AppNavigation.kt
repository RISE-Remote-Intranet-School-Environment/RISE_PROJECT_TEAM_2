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
import rise_front_end.team2.ui.screens.StudentHelpForum.detail.StudentHelpForumDetailScreen
import rise_front_end.team2.ui.screens.StudentHelpForum.list.StudentHelpForumListScreen
import rise_front_end.team2.ui.screens.syllabus.detail.SyllabusDetailScreen
import rise_front_end.team2.ui.screens.syllabus.list.SyllabusListScreen
import rise_front_end.team2.ui.screens.screens_grades.GradeScreen
import rise_front_end.team2.ui.screens.screens_grades.RegistrationScreen
import rise_front_end.team2.ui.screens.screens_profil.ProfileScreen
import rise_front_end.team2.ui.theme.AppTheme

@Composable
actual fun PlatformNavigation() {
    AppTheme {
        val navController: NavHostController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route


        Scaffold(

            topBar = {

                TopBar(getScreenTitle(currentRoute),
                    onProfilClick = {
                    navController.navigate(Screens.ProfileScreen.route)
                })
            },
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
                            navController.navigate(Screens.SyllabusListDestination.route)
                        },
                        onFileHostingClick = {
                            navController.navigate(Screens.FileHostingScreen.route)
                        },
                        onStudentHelpForumClick = {
                            navController.navigate(Screens.StudentHelpForumList.route)

                        }

                    )
                }

                // Main Navigation Screens
                composable(route = Screens.GradeScreen.route) {
                    GradeScreen(
                        onRegistrationClick = {
                            navController.navigate(Screens.RegistrationScreen.route)
                        }
                    )
                }

                composable(route = Screens.RegistrationScreen.route) {
                    RegistrationScreen()
                }

                composable(route = Screens.ProfileScreen.route) {
                    ProfileScreen()
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

                // SyllabusList Screen
                composable(route = Screens.SyllabusListDestination.route) {
                    SyllabusListScreen(
                        navigateToDetails = { objectId ->
                            navController.navigate("detail/$objectId")
                        }
                    )
                }

                // SyllabusDetail Screen
                composable(
                    route = Screens.SyllabusDetailDestination.route,
                    arguments = listOf(
                        navArgument("objectId") {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->
                    val objectId = backStackEntry.arguments?.getInt("objectId") ?: 0
                    SyllabusDetailScreen(
                        objectId = objectId,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                }


                // Student Help forum list Screen
                composable(route = Screens.StudentHelpForumList.route) {
                    StudentHelpForumListScreen(
                        navigateToDetails = { objectId ->
                            navController.navigate("studenthelpForumdetail/$objectId")
                        }
                    )
                }

                // Student Help Forum Detail Screen
                composable(
                    route = Screens.StudentHelpForumDetail.route,
                    arguments = listOf(
                        navArgument("objectId") {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->
                    val objectId = backStackEntry.arguments?.getInt("objectId") ?: 0
                    StudentHelpForumDetailScreen(
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

fun getScreenTitle(route: String?): String {
    return when (route) {
        Screens.HomeScreen.route -> "Home"
        Screens.GradeScreen.route -> "Grades"
        Screens.RegistrationScreen.route -> "Registration"
        Screens.ProfileScreen.route -> "Profile"
        Screens.FavoriteScreen.route -> "Favorites"
        Screens.CalendarScreen.route -> "Calendar"
        Screens.SyllabusScreen.route -> "Syllabus"
        Screens.FileHostingScreen.route -> "File Hosting"
        else -> "App"
    }
}