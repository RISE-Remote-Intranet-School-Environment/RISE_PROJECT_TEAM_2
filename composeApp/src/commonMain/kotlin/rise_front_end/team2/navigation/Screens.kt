package rise_front_end.team2.navigation

sealed class Screens(val route: String) {
    object HomeScreen : Screens("home")
    object GradeScreen : Screens("grade")
    object FavoritesScreen : Screens("favorites")
    object CalendarScreen : Screens("calendar")
    object SyllabusScreen : Screens("syllabus")
    object FileHostingScreen : Screens("filehosting")
    object SyllabusListDestination : Screens("list")
    object ListDestination : Screens("list")
    object RegistrationScreen : Screens("registration")
    object ProfileScreen : Screens("profil")
    object StudentHelpForumList : Screens("studentHelpForumList")
    object ShopScreen : Screens("shop")


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

    class CourseFiles(courseId: Int) : Screens("courseFiles/$courseId") {
        companion object {
            const val route = "courseFiles/{courseId}"
        }
    }

    class FileDiscussions(courseId: Int, fileId: Int) : Screens("fileDiscussions/$courseId/$fileId") {
        companion object {
            const val route = "fileDiscussions/{courseId}/{fileId}"
        }
    }
}
