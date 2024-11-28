package rise_front_end.team2.navigation

sealed class Screens(val route: String) {
    object HomeScreen : Screens("home")
    object GradeScreen : Screens("grade")
    object FavoriteScreen : Screens("favorite")
    object CalendarScreen : Screens("calendar")
    object SyllabusScreen : Screens("syllabus")
    object FileHostingScreen : Screens("filehosting")
    object SyllabusListDestination : Screens("list")
    object ListDestination : Screens("list")
    object RegistrationScreen : Screens("registration")
    object ProfileScreen : Screens("profil")
    object StudentHelpForumList : Screens("studentHelpForumList")


    class SyllabusDetailDestination(objectId: Int) : Screens("detail/$objectId") {
        companion object {
            const val route = "detail/{objectId}"
        }
    }


    class StudentHelpForumDetail(courseId: Int) : Screens("studentHelpForumDetail/$courseId") {
        companion object {
            const val route = "studentHelpForumDetail/{courseId}"
        }
    }
    class StudentHelpForumMessageAnswers(courseId: Int, messageId: Int) :
        Screens("studentHelpForumMessageAnswers/$courseId/$messageId") {
        companion object {
            const val route = "studentHelpForumMessageAnswers/{courseId}/{messageId}"
        }
    }
}
