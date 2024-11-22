package rise_front_end.team2.navigation

sealed class Screens(val route: String) {
    object HomeScreen : Screens("home")
    object GradeScreen : Screens("grade")
    object FavoriteScreen : Screens("favorite")
    object CalendarScreen : Screens("calendar")
    object SyllabusScreen : Screens("syllabus")
    object FileHostingScreen : Screens("filehosting")
    object SyllabusListDestination : Screens("list")
    object StudentHelpForumList : Screens("studentHelpForumList")


    class SyllabusDetailDestination(objectId: Int) : Screens("detail/$objectId") {
        companion object {
            const val route = "detail/{objectId}"
        }
    }

    class StudentHelpForumDetail(objectId: Int) : Screens("studenthelpForumdetail/$objectId") {
        companion object {
            const val route = "studenthelpForumdetail/{objectId}"
        }
    }
}
