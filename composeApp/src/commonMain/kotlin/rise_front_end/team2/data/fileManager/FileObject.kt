package rise_front_end.team2.data.fileManager

import kotlinx.serialization.Serializable

@Serializable
data class FileObject(
    val fileID: String,
    val fileName: String,
    val fileExtension: String,
    val fileSize: String,
    val filePrimaryImage: String,
    val filePrimaryImageSmall: String
)