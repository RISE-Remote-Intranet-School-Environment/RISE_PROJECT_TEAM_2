package rise_front_end.team2.data.syllabus

import kotlinx.serialization.Serializable

@Serializable
data class SyllabusObject(
    val objectID: Int,
    val title: String,
    val artistDisplayName: String,
    val medium: String,
    val duration: String,
    val objectURL: String,
    val objectDate: String,
    val primaryImage: String,
    val primaryImageSmall: String,
    val university: String,
    val department: String,
    val creditLine: String,
    val price : Int
)
