package rise_front_end.team2.data.FileManager

import kotlinx.serialization.Serializable

@Serializable
data class FileObject(
    val fileObjectID: String,
    val fileName: String,
    val dataLink: String,
    val previewLink: String,
    val size: Float
)