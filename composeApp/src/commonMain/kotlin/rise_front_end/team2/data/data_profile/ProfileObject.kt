package rise_front_end.team2.data.data_profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileObject(
    val objectID: Int,
    val userName: String,
    val mail: String,
    val password: String,
    val className: String,
    val option: String,
    val year: Int,
    val points: Int,
)
