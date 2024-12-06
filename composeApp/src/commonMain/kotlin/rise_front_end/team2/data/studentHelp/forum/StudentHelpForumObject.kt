package rise_front_end.team2.data.studentHelp.forum

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val courseID: Int,
    val courseName: String,
    val teacherName: String,
    val courseYear: String,
    val forum: List<ForumMessage> // List of forum posts for the course
)

@Serializable
data class ForumMessage(
    val messageID: Int,
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
