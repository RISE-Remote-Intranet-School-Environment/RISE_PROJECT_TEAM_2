package rise_front_end.team2.data.studentHelp.forum

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface StudentHelpForumStorage {
    suspend fun saveCourses(newCourses: List<Course>)
    fun getCourses(): Flow<List<Course>>
    fun getCourseById(courseId: Int): Flow<Course?>
    fun getForumMessageById(courseId: Int, messageId: Int): Flow<ForumMessage?>
    fun getFileById(courseId: Int, fileId: Int): Flow<CourseFile?>
    fun getFileMessageById(courseId: Int, fileId: Int, messageId: Int): Flow<FileMessage?>
    suspend fun addForumMessage(courseId: Int, message: ForumMessage): Boolean
    suspend fun updateForumMessage(courseId: Int, messageId: Int, newContent: String): Boolean
    suspend fun addAnswer(courseId: Int, messageId: Int, answer: Answer): Boolean
    fun getTagsForCourse(courseId: Int): Flow<List<String>>
    suspend fun likeAnswer(courseId: Int, messageId: Int, answerId: Int): Boolean
    suspend fun likeFile(courseId: Int, fileId: Int): Boolean

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

    override fun getFileById(courseId: Int, fileId: Int): Flow<CourseFile?> {
        return storedCourses.map { courses ->
            courses.find { it.courseID == courseId }
                ?.courseFiles
                ?.find { it.fileID == fileId }
        }
    }

    override fun getFileMessageById(courseId: Int, fileId: Int, messageId: Int): Flow<FileMessage?> {
        return storedCourses.map { courses ->
            courses.find { it.courseID == courseId }
                ?.courseFiles
                ?.find { it.fileID == fileId }
                ?.messages
                ?.find { it.messageID == messageId }
        }
    }
    override suspend fun addForumMessage(courseId: Int, message: ForumMessage): Boolean {
        val currentCourses = storedCourses.value.toMutableList()
        val courseIndex = currentCourses.indexOfFirst { it.courseID == courseId }

        if (courseIndex != -1) {
            // Generate a unique messageID
            val newMessage = message.copy(
                messageID = System.currentTimeMillis().toInt(),
                timestamp = System.currentTimeMillis().toString()
            )

            val updatedCourses = currentCourses.toMutableList()
            val updatedCourse = updatedCourses[courseIndex].copy(
                forum = updatedCourses[courseIndex].forum + newMessage
            )
            updatedCourses[courseIndex] = updatedCourse

            storedCourses.value = updatedCourses
            return true
        }

        return false
    }

    override suspend fun updateForumMessage(courseId: Int, messageId: Int, newContent: String): Boolean {
        val currentCourses = storedCourses.value.toMutableList()
        val courseIndex = currentCourses.indexOfFirst { it.courseID == courseId }

        if (courseIndex != -1) {
            val messageIndex = currentCourses[courseIndex].forum.indexOfFirst { it.messageID == messageId }

            if (messageIndex != -1) {
                val updatedCourses = currentCourses.toMutableList()
                val updatedForum = updatedCourses[courseIndex].forum.toMutableList()
                val updatedMessage = updatedForum[messageIndex].copy(
                    content = newContent,
                    timestamp = System.currentTimeMillis().toString()
                )
                updatedForum[messageIndex] = updatedMessage

                val updatedCourse = updatedCourses[courseIndex].copy(forum = updatedForum)
                updatedCourses[courseIndex] = updatedCourse

                storedCourses.value = updatedCourses
                return true
            }
        }

        return false
    }

    override suspend fun addAnswer(courseId: Int, messageId: Int, answer: Answer): Boolean {
        val currentCourses = storedCourses.value.toMutableList()
        val courseIndex = currentCourses.indexOfFirst { it.courseID == courseId }

        if (courseIndex != -1) {
            val courseForums = currentCourses[courseIndex].forum.toMutableList()
            val messageIndex = courseForums.indexOfFirst { it.messageID == messageId }

            if (messageIndex != -1) {
                // Generate a unique answerID
                val newAnswer = answer.copy(
                    answerID = System.currentTimeMillis().toInt(),
                    timestamp = System.currentTimeMillis().toString()
                )

                // Create an updated message with the new answer
                val updatedMessage = courseForums[messageIndex].copy(
                    answers = courseForums[messageIndex].answers + newAnswer
                )

                // Replace the old message with the updated one
                courseForums[messageIndex] = updatedMessage

                // Update the course's forum list
                val updatedCourses = currentCourses.toMutableList()
                updatedCourses[courseIndex] = updatedCourses[courseIndex].copy(
                    forum = courseForums
                )

                storedCourses.value = updatedCourses
                return true
            }
        }

        return false
    }
    override fun getTagsForCourse(courseId: Int): Flow<List<String>> {
        return storedCourses.map { courses ->
            courses
                .find { it.courseID == courseId }
                ?.courseFiles
                ?.flatMap { it.tags }
                ?.distinct()
                ?.sorted()
                ?: emptyList()
        }
    }

    override suspend fun likeAnswer(courseId: Int, messageId: Int, answerId: Int): Boolean {
        val currentCourses = storedCourses.value.toMutableList()
        val courseIndex = currentCourses.indexOfFirst { it.courseID == courseId }

        if (courseIndex != -1) {
            val courseForums = currentCourses[courseIndex].forum.toMutableList()
            val messageIndex = courseForums.indexOfFirst { it.messageID == messageId }

            if (messageIndex != -1) {
                val updatedAnswers = courseForums[messageIndex].answers.map { answer ->
                    if (answer.answerID == answerId) {
                        // Toggle likes - increment if not liked, decrement if already liked
                        answer.copy(
                            likes = if (answer.likes >= 0) answer.likes + 1 else 0
                        )
                    } else {
                        answer
                    }
                }

                // Update the message with modified answers
                val updatedMessage = courseForums[messageIndex].copy(answers = updatedAnswers)
                courseForums[messageIndex] = updatedMessage

                // Update the courses
                val updatedCourses = currentCourses.toMutableList()
                updatedCourses[courseIndex] = updatedCourses[courseIndex].copy(
                    forum = courseForums
                )

                storedCourses.value = updatedCourses
                return true
            }
        }

        return false
    }

    override suspend fun likeFile(courseId: Int, fileId: Int): Boolean {
        val currentCourses = storedCourses.value.toMutableList()
        val courseIndex = currentCourses.indexOfFirst { it.courseID == courseId }

        if (courseIndex != -1) {
            val updatedCourseFiles = currentCourses[courseIndex].courseFiles.map { courseFile ->
                if (courseFile.fileID == fileId) {
                    courseFile.copy(
                        fileLikes = courseFile.fileLikes + 1
                    )
                } else {
                    courseFile
                }
            }

            // Create an updated course with the modified course files
            val updatedCourses = currentCourses.toMutableList()
            updatedCourses[courseIndex] = updatedCourses[courseIndex].copy(
                courseFiles = updatedCourseFiles
            )

            // Update the stored courses
            storedCourses.value = updatedCourses
            return true
        }

        return false
    }



}
