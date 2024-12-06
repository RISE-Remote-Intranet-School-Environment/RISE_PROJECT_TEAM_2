package rise_front_end.team2.data.data_grades

import kotlinx.serialization.Serializable

@Serializable
data class GradesObject(
    val objectID: Int,
    val nameUe: String,
    val ects: Int,
    val list: List<GradesDetailObject>,

)

@Serializable
data class GradesDetailObject(
    val name: String,
    val grades: Int,
    val ectsNumber: Int,
)