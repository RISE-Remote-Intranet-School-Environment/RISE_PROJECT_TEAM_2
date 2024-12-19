package rise_front_end.team2.Repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import rise_front_end.team2.data.studentHelp.forum.Answer
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

    //Add a forum post
    suspend fun addForumMessage(courseId: Int, message: ForumMessage): Boolean {
        return studentHelpForumStorage.addForumMessage(courseId, message)
    }

    //Update forum posts
    suspend fun updateForumMessage(courseId: Int, messageId: Int, newContent: String): Boolean {
        return studentHelpForumStorage.updateForumMessage(courseId, messageId, newContent)
    }

    suspend fun addAnswer(courseId: Int, messageId: Int, answer: Answer): Boolean {
        return studentHelpForumStorage.addAnswer(courseId, messageId, answer)
    }


    fun getTagsForCourse(courseId: Int): Flow<List<String>> {
        return studentHelpForumStorage.getTagsForCourse(courseId)
    }

    suspend fun likeAnswer(courseId: Int, messageId: Int, answerId: Int): Boolean {
        return studentHelpForumStorage.likeAnswer(courseId, messageId, answerId)
    }

    suspend fun likeFile(courseId: Int, fileId: Int): Boolean{
        return studentHelpForumStorage.likeFile(courseId, fileId)
    }

    suspend fun addNewFile(courseId: Int, fileName: String, fileUrl: String): Boolean{
        return studentHelpForumStorage.addNewFile(courseId, fileName, fileUrl)
    }

    suspend fun addFileAnswer(courseId: Int, fileId: Int, message: FileMessage): Boolean {
        return studentHelpForumStorage.addFileAnswer(courseId, fileId, message)
    }



    }
