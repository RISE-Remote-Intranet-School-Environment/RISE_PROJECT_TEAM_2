package rise_front_end.team2.data.CourseManager

import kotlinx.serialization.Serializable

@Serializable
data class CourseObject(
    val courseObjectID: String,
    val courseObjectName: String
)