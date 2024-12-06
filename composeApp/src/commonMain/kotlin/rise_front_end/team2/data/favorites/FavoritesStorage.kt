package rise_front_end.team2.data.favorites

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface FavoriteStorage {
    suspend fun saveFavorites(newFavorites : List<FavoritesObject>)
    fun getFavorites(): Flow<List<FavoritesObject>>
    fun getFavoriteCourseByID(courseID : Int): Flow<FavoritesCourseObject?>
    fun getFavoriteFileByID(courseID: Int, fileID : Int): Flow<FavoritesFileObject?>
}

class InMemoryFavoritesStorage : FavoriteStorage {
    private val storedFavorites = MutableStateFlow(emptyList<FavoritesObject>())

    override suspend fun saveFavorites(newFavorites: List<FavoritesObject>) {
        storedFavorites.value = newFavorites
    }

    override fun getFavorites(): Flow<List<FavoritesObject>> = storedFavorites

    override fun getFavoriteCourseByID(courseID: Int): Flow<FavoritesCourseObject?> {
        return storedFavorites.map { favorites ->
            favorites.filterIsInstance<FavoritesCourseObject>()
                .find { it.courseID == courseID }
        }
    }

    override fun getFavoriteFileByID(courseID: Int, fileID: Int): Flow<FavoritesFileObject?> {
        return storedFavorites.map { favorites ->
            favorites.filterIsInstance<FavoritesFileObject>()
                .find { it.courseID == courseID && it.fileID == fileID }
        }
    }
}