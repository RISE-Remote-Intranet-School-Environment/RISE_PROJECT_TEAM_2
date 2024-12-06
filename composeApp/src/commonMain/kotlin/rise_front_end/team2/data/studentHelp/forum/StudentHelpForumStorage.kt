package rise_front_end.team2.data.studentHelp.forum

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface StudentHelpForumStorage {
    suspend fun saveCourses(newCourses: List<Course>)
    fun getCourses(): Flow<List<Course>>
    fun getCourseById(courseId: Int): Flow<Course?>
    fun getForumMessageById(courseId: Int, messageId: Int): Flow<ForumMessage?>
}


class InMemoryStudentHelpForumStorage : StudentHelpForumStorage {
    private val storedCourses = MutableStateFlow(emptyList<Course>())

    override suspend fun saveCourses(newCourses: List<Course>) {
        storedCourses.value = newCourses
    }

    override fun getCourses(): Flow<List<Course>> = storedCourses

    override fun getCourseById(courseId: Int): Flow<Course?> {
        return storedCourses.map { courses -> courses.find { it.courseID == courseId } }
    }

    override fun getForumMessageById(courseId: Int, messageId: Int): Flow<ForumMessage?> {
        return storedCourses.map { courses ->
            courses.find { it.courseID == courseId }
                ?.forum
                ?.find { it.messageID == messageId }
        }
    }
}

