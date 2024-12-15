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
import rise.front_end.team2.ui.screens.studentHelp.files.filesList.CourseFilesListScreen
import rise_front_end.team2.ui.screens.*
import rise_front_end.team2.ui.screens.StudentHelpForum.answer.ForumMessageAnswersScreen
import rise_front_end.team2.ui.screens.StudentHelpForum.courseslist.StudentHelpCourseListScreen
import rise_front_end.team2.ui.screens.StudentHelpForum.posts.StudentHelpForumPostsScreen
import rise_front_end.team2.ui.screens.syllabus.detail.SyllabusDetailScreen
import rise_front_end.team2.ui.screens.syllabus.list.SyllabusListScreen
import rise_front_end.team2.ui.screens.favorites.FavoritesScreen
import rise_front_end.team2.ui.screens.screens_grades.GradeScreen
import rise_front_end.team2.ui.screens.screens_grades.RegistrationScreen
import rise_front_end.team2.ui.screens.screens_profil.ProfileScreen
import rise_front_end.team2.ui.screens.screens_profil.ShopScreen
import rise_front_end.team2.ui.screens.studentHelp.files.fileanswers.FileDiscussionsScreen
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
                    ProfileScreen(
                        onShopClick = {
                            navController.navigate(Screens.ShopScreen.route)
                        }
                    )
                }

                composable(route = Screens.ShopScreen.route) {
                    ShopScreen()
                }

                composable(route = Screens.FavoritesScreen.route) {
                    FavoritesScreen(
                        navigateToCourse = { courseId ->
                            navController.navigate(Screens.StudentHelpForumDetail(courseId).route)
                        },
                        navigateToFile = { courseId, fileId ->
                            navController.navigate(Screens.FileDiscussions(courseId, fileId).route)
                        }
                    )
                }

                composable(route = Screens.CalendarScreen.route) {
                    CalendarScreen()
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


                // Student Help Forum List Screen
                composable(route = Screens.StudentHelpForumList.route) {
                    StudentHelpCourseListScreen(

                        navigateToForum = { courseId ->
                            navController.navigate(Screens.StudentHelpForumDetail(courseId).route)
                        },
                        navigateToFiles = { courseId ->
                            navController.navigate(Screens.CourseFiles(courseId).route)
                        }
                    )
                }


                // Student Help Forum Detail Screen
                composable(
                    route = Screens.StudentHelpForumDetail.route,
                    arguments = listOf(
                        navArgument("courseId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
                    StudentHelpForumPostsScreen(
                        courseId = courseId,
                        navigateToAnswers = { courseId, messageId ->
                            navController.navigate(
                                Screens.StudentHelpForumMessageAnswers(courseId, messageId).route
                            )
                        },
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                }

                // Forum Message Answers Screen
                composable(
                    route = Screens.StudentHelpForumMessageAnswers.route,
                    arguments = listOf(
                        navArgument("courseId") { type = NavType.IntType },
                        navArgument("messageId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
                    val messageId = backStackEntry.arguments?.getInt("messageId") ?: 0
                    ForumMessageAnswersScreen(
                        courseId = courseId,
                        messageId = messageId,
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                }

                // Course Files Screen
                composable(
                    route = Screens.CourseFiles.route,
                    arguments = listOf(
                        navArgument("courseId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
                    CourseFilesListScreen(
                        courseId = courseId,
                        navigateToFileDiscussions = { courseId, fileId ->
                            navController.navigate(
                                Screens.FileDiscussions(courseId, fileId).route
                            )
                        },
                        navigateBack = {
                            navController.popBackStack()
                        }
                    )
                }

                // File Discussions Screen
                composable(
                    route = Screens.FileDiscussions.route,
                    arguments = listOf(
                        navArgument("courseId") { type = NavType.IntType },
                        navArgument("fileId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
                    val fileId = backStackEntry.arguments?.getInt("fileId") ?: 0
                    FileDiscussionsScreen(
                        courseId = courseId,
                        fileId = fileId,
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
        Screens.FavoritesScreen.route -> "Favorites"
        Screens.CalendarScreen.route -> "Calendar"
        Screens.SyllabusListDestination.route -> "Syllabus"
        Screens.SyllabusDetailDestination.route -> "Syllabus"
        Screens.ShopScreen.route -> "Shop"
        else -> "App"
    }
}