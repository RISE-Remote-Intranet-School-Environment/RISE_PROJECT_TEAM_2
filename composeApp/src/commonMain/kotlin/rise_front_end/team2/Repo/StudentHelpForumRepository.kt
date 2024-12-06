package rise_front_end.team2.Repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.data.studentHelp.forum.ForumMessage
import rise_front_end.team2.data.studentHelp.forum.StudentHelpForumApi
import rise_front_end.team2.data.studentHelp.forum.StudentHelpForumStorage
import rise_front_end.team2.data.studentHelp.forum.Course
import rise_front_end.team2.data.studentHelp.forum.CourseFile
import rise_front_end.team2.data.studentHelp.forum.FileMessage


class StudentHelpForumRepository(
    private val studentHelpForumApi: StudentHelpForumApi,
    private val studentHelpForumStorage: StudentHelpForumStorage,
) {
    private val scope = CoroutineScope(SupervisorJob())

    // Initialize the repository by fetching data from the API and saving it to storage.
    fun initialize() {
        scope.launch {
            refresh()
        }
    }

    // Fetch data from the API and save it to the storage.
    suspend fun refresh() {
        val courses = studentHelpForumApi.getData()
        studentHelpForumStorage.saveCourses(courses)
    }

    // Get the list of courses.
    fun getCourses(): Flow<List<Course>> = studentHelpForumStorage.getCourses()

    // Get a specific course by its ID.
    fun getCourseById(courseId: Int): Flow<Course?> = studentHelpForumStorage.getCourseById(courseId)

    // Get a specific forum message by course ID and message ID.
    fun getForumMessageById(courseId: Int, messageId: Int): Flow<ForumMessage?> =
        studentHelpForumStorage.getForumMessageById(courseId, messageId)

    // Get a specific file by course ID and file ID.
    fun getFileById(courseId: Int, fileId: Int): Flow<CourseFile?> =
        studentHelpForumStorage.getFileById(courseId, fileId)

    // Get a specific file message by course ID, file ID, and message ID.
    fun getFileMessageById(courseId: Int, fileId: Int, messageId: Int): Flow<FileMessage?> =
        studentHelpForumStorage.getFileMessageById(courseId, fileId, messageId)
}