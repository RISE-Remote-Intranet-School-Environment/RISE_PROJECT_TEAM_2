package rise_front_end.team2.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import rise_front_end.team2.Repo.FavoritesRepository
import rise_front_end.team2.data.favorites.FavoritesObject

class FavoritesViewModel (
    private val repository: FavoritesRepository
    ) : ViewModel() {
        val favorites: StateFlow<List<FavoritesObject>> =
            repository.getFavorites()
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}