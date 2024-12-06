package rise_front_end.team2.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import rise_front_end.team2.Repo.FavoritesRepository
import rise_front_end.team2.data.favorites.FavoritesObject
import rise_front_end.team2.data.favorites.FavoritesCourseObject
import rise_front_end.team2.data.favorites.FavoritesFileObject

class FavoritesViewModel (
    private val repository: FavoritesRepository
    ) : ViewModel() {

    // StateFlow for all favorites
    val favorites: StateFlow<List<FavoritesObject>> =
        repository.getFavorites()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // StateFlow for courses only
    val favoriteCourses: StateFlow<List<FavoritesCourseObject>> =
        repository.getFavorites()
            .map { list -> list.filterIsInstance<FavoritesCourseObject>() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // StateFlow for files only
    val favoriteFiles: StateFlow<List<FavoritesFileObject>> =
        repository.getFavorites()
            .map { list -> list.filterIsInstance<FavoritesFileObject>() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Refresh favorites from API
    fun refreshFavorites() {
        viewModelScope.launch {
            repository.refresh()
        }
    }

    // Get a specific course by ID
    fun getFavoriteCourseByID(courseID: Int): StateFlow<FavoritesCourseObject?> =
        repository.getFavoriteCourseByID(courseID)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Get a specific file by course ID and file ID
    fun getFavoriteFileByID(courseID: Int, fileID: Int): StateFlow<FavoritesFileObject?> =
        repository.getFavoriteFileByID(courseID, fileID)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}