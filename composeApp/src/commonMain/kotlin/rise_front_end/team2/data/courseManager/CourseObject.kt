package rise_front_end.team2.data.courseManager

import kotlinx.serialization.Serializable
import rise_front_end.team2.data.fileManager.FileObject

@Serializable
data class CourseObject(
    val courseID: String,
    val courseName: String,
    val files: List<FileObject>
) {
    val courseObjectID: String
        get() = courseID

    val courseObjectName: String
        get() = courseName

    val courseObjectFiles: List<FileObject>
        get() = files
}