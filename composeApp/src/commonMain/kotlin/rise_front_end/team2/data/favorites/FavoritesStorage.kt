package rise_front_end.team2.data.favorites

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface FavoriteStorage {
    suspend fun saveFavorites(newFavorites : List<FavoritesObject>)
    fun getFavorites(): Flow<List<FavoritesObject>>
    fun getFavoriteByID(favoriteID : Int): Flow<FavoritesObject?>
}

class InMemoryFavoritesStorage : FavoriteStorage {
    private val storedFavorites = MutableStateFlow(emptyList<FavoritesObject>())

    override suspend fun saveFavorites(newFavorites: List<FavoritesObject>) {
        storedFavorites.value = newFavorites
    }

    override fun getFavorites(): Flow<List<FavoritesObject>> = storedFavorites

    override fun getFavoriteByID(linkID: Int): Flow<FavoritesObject?> {
        return storedFavorites.map { favorites ->
            favorites.find { it.linkID == linkID }
        }
    }
}