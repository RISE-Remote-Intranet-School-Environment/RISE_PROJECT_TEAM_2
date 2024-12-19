package rise_front_end.team2.data.studentHelp.forum

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface StudentHelpForumStorage {

    /**
     * Saves a list of courses to the storage.
     * @param newCourses The list of courses to save.
     */
    suspend fun saveCourses(newCourses: List<Course>)

    /**
     * Retrieves all stored courses.
     * @return A Flow emitting a list of courses.
     */
    fun getCourses(): Flow<List<Course>>

    /**
     * Retrieves a specific course by its ID.
     * @param courseId The ID of the course to retrieve.
     * @return A Flow emitting the course or null if not found.
     */
    fun getCourseById(courseId: Int): Flow<Course?>

    /**
     * Retrieves a forum message by course ID and message ID.
     * @param courseId The ID of the course containing the message.
     * @param messageId The ID of the message to retrieve.
     * @return A Flow emitting the forum message or null if not found.
     */
    fun getForumMessageById(courseId: Int, messageId: Int): Flow<ForumMessage?>

    /**
     * Retrieves a file by its ID within a course.
     * @param courseId The ID of the course containing the file.
     * @param fileId The ID of the file to retrieve.
     * @return A Flow emitting the file or null if not found.
     */
    fun getFileById(courseId: Int, fileId: Int): Flow<CourseFile?>

    /**
     * Retrieves a message associated with a specific file in a course.
     * @param courseId The ID of the course containing the file.
     * @param fileId The ID of the file containing the message.
     * @param messageId The ID of the message to retrieve.
     * @return A Flow emitting the file message or null if not found.
     */
    fun getFileMessageById(courseId: Int, fileId: Int, messageId: Int): Flow<FileMessage?>

    /**
     * Adds a forum message to a specific course.
     * @param courseId The ID of the course.
     * @param message The forum message to add.
     * @return True if the message was added successfully, false otherwise.
     */
    suspend fun addForumMessage(courseId: Int, message: ForumMessage): Boolean

    /**
     * Updates the content of a forum message.
     * @param courseId The ID of the course containing the message.
     * @param messageId The ID of the message to update.
     * @param newContent The new content for the message.
     * @return True if the message was updated successfully, false otherwise.
     */
    suspend fun updateForumMessage(courseId: Int, messageId: Int, newContent: String): Boolean

    /**
     * Adds an answer to a forum message.
     * @param courseId The ID of the course.
     * @param messageId The ID of the forum message.
     * @param answer The answer to add.
     * @return True if the answer was added successfully, false otherwise.
     */
    suspend fun addAnswer(courseId: Int, messageId: Int, answer: Answer): Boolean

    /**
     * Retrieves tags associated with a specific course.
     * @param courseId The ID of the course.
     * @return A Flow emitting a list of tags.
     */
    fun getTagsForCourse(courseId: Int): Flow<List<String>>

    /**
     * Likes an answer to a forum message.
     * @param courseId The ID of the course.
     * @param messageId The ID of the forum message.
     * @param answerId The ID of the answer to like.
     * @return True if the answer was liked successfully, false otherwise.
     */
    suspend fun likeAnswer(courseId: Int, messageId: Int, answerId: Int): Boolean

    /**
     * Likes a file associated with a course.
     * @param courseId The ID of the course.
     * @param fileId The ID of the file to like.
     * @return True if the file was liked successfully, false otherwise.
     */
    suspend fun likeFile(courseId: Int, fileId: Int): Boolean

    /**
     * Adds a new file to a course.
     * @param courseId The ID of the course.
     * @param fileName The name of the file.
     * @param fileUrl The URL of the file.
     * @return True if the file was added successfully, false otherwise.
     */
    suspend fun addNewFile(courseId: Int, fileName: String, fileUrl: String): Boolean

    /**
     * Adds a message to a specific file within a course.
     * @param courseId The ID of the course.
     * @param fileId The ID of the file.
     * @param message The message to add.
     * @return True if the message was added successfully, false otherwise.
     */
    suspend fun addFileAnswer(courseId: Int, fileId: Int, message: FileMessage): Boolean
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

    override suspend fun addNewFile(courseId: Int, fileName: String, fileUrl: String): Boolean {
        val currentCourses = storedCourses.value.toMutableList()
        val courseIndex = currentCourses.indexOfFirst { it.courseID == courseId }

        if (courseIndex != -1) {
            // Create new file with generated ID and default values
            val newFile = CourseFile(
                fileID = System.currentTimeMillis().toInt(),
                fileName = fileName,
                fileUrl = fileUrl,
                fileDate = System.currentTimeMillis().toString(),
                inFavorites = false,
                fileLikes = 0,
                messages = emptyList(),
                tags = emptyList(),
                fileAuthor = "Current Author", //Need to change it to a real username
                profilePicture = "https://i.imgur.com/0fvzn7p.png" // Default profile picture
            )

            val updatedCourses = currentCourses.toMutableList()
            val updatedCourse = updatedCourses[courseIndex].copy(
                courseFiles = updatedCourses[courseIndex].courseFiles + newFile
            )
            updatedCourses[courseIndex] = updatedCourse

            storedCourses.value = updatedCourses
            return true
        }

        return false
    }


    override suspend fun addFileAnswer(courseId: Int, fileId: Int, message: FileMessage): Boolean {
        val currentCourses = storedCourses.value.toMutableList()
        val courseIndex = currentCourses.indexOfFirst { it.courseID == courseId }

        if (courseIndex != -1) {
            val courseFiles = currentCourses[courseIndex].courseFiles.toMutableList()
            val fileIndex = courseFiles.indexOfFirst { it.fileID == fileId }

            if (fileIndex != -1) {
                // Create an updated file with the new message
                val updatedFile = courseFiles[fileIndex].copy(
                    messages = courseFiles[fileIndex].messages + message
                )

                // Replace the old file with the updated one
                courseFiles[fileIndex] = updatedFile

                // Update the course's files list
                val updatedCourses = currentCourses.toMutableList()
                updatedCourses[courseIndex] = updatedCourses[courseIndex].copy(
                    courseFiles = courseFiles
                )

                storedCourses.value = updatedCourses
                return true
            }
        }

        return false
    }



}
