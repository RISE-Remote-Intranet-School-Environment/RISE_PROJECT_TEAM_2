package rise_front_end.team2.data.courseManager

import kotlinx.serialization.Serializable

@Serializable
data class CourseObject(
    val courseID: String,
    val courseName: String
) {
    val courseObjectID: String
        get() = courseID

    val courseObjectName: String
        get() = courseName
}