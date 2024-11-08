package rise_front_end.team2.ui.screens.list_syllabus

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import rise_front_end.team2.ui.Data.data_syllabus.Museum
import rise_front_end.team2.ui.Data.data_syllabus.MuseumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ListScreenModel(museumRepository: MuseumRepository) : ScreenModel {
    val objects: StateFlow<List<Museum>> =
        museumRepository.getMuseum()
            .stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
