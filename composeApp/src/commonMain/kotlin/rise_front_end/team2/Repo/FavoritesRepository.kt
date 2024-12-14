package rise_front_end.team2.Repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.data.favorites.FavoritesObject
import rise_front_end.team2.data.favorites.FavoriteStorage
import rise_front_end.team2.data.favorites.FavoriteApi
import rise_front_end.team2.data.favorites.FavoritesCourseObject
import rise_front_end.team2.data.favorites.FavoritesFileObject

class FavoritesRepository(
    private val favoriteStorage: FavoriteStorage,
    private val favoriteApi: FavoriteApi
) {
    private val scope = CoroutineScope(SupervisorJob())

    fun initialize() {
        scope.launch {
            refresh()
        }
    }

    suspend fun refresh() {
        favoriteStorage.saveFavorites(favoriteApi.getData())
    }

    fun getFavorites(): Flow<List<FavoritesObject>> = favoriteStorage.getFavorites()

    fun getFavoriteCourseByID(courseID: Int): Flow<FavoritesCourseObject?> =
        favoriteStorage.getFavoriteCourseByID(courseID)

    fun getFavoriteFileByID(courseID: Int, fileID: Int): Flow<FavoritesFileObject?> =
        favoriteStorage.getFavoriteFileByID(courseID, fileID)


}