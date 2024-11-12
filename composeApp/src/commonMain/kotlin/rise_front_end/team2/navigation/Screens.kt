package rise_front_end.team2.navigation

sealed class Screens(val route: String) {
    // What's in the navbar
    object HomeScreen : Screens("home")
    object GradeScreen : Screens("grade")
    object FavoriteScreen : Screens("favorite")
    object CalendarScreen : Screens("calendar")

    // Not in the navbar
    object SyllabusScreen : Screens("syllabus")
    object FileHostingScreen : Screens("fileHosting")

    // New screens with parameters
    object ListDestination : Screens("list")

    data class DetailDestination(val objectId: Int) : Screens("detail/$objectId")
}
