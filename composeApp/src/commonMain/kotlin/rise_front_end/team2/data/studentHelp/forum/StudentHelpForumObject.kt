package rise_front_end.team2.data.studentHelp.forum

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val courseID: Int,
    val courseName: String,
    val teacherName: String,
    val courseYear: String,
    val inFavorites: Boolean,
    val forum: List<ForumMessage>,
    val courseFiles: List<CourseFile>

)

@Serializable
data class ForumMessage(
    val messageID: Int,
    val title: String,
    val content: String,
    val author: String,
    val timestamp: String,
    val answers: List<Answer> // List of answers for the forum post
)

@Serializable
data class Answer(
    val answerID: Int,
    val content: String,
    val author: String,
    val timestamp: String,
    val likes: Int // the number of likes of an answer
)

@Serializable
data class CourseFile(
    val fileID: Int,
    val fileName: String,
    val fileUrl: String,
    val inFavorites: Boolean,
    val fileLikes: Int = 0, // Default to 0 if missing
    val fileAuthor: String = "Unknown Author", // Default to a placeholder if missing
    val fileDate: String = "Unknown Date", // Default to a placeholder if missing
    val messages: List<FileMessage> = emptyList() // Default to empty list if missing
)

@Serializable
data class FileMessage(
    val messageID: Int,
    val content: String = "Content Missing",
    val author: String,
    val timestamp: String,
    val likes: Int
)
