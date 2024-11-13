package rise_front_end.team2.ui.screens.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import rise_front_end.team2.data.SyllabusObject
import rise_front_end.team2.Repo.SyllabusRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ListViewModel(museumRepository: SyllabusRepository) : ViewModel() {
    val objects: StateFlow<List<SyllabusObject>> =
        museumRepository.getObjects()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}