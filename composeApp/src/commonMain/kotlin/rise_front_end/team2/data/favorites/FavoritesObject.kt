package rise_front_end.team2.data.favorites

import kotlinx.serialization.Serializable

@Serializable
sealed class FavoritesObject {
    abstract val courseID: Int
}

@Serializable
data class FavoritesCourseObject(
    override val courseID: Int
) : FavoritesObject()

@Serializable
data class FavoritesFileObject(
    override val courseID: Int,
    val fileID: Int
) : FavoritesObject()