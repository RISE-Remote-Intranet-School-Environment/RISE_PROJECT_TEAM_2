package rise_front_end.team2.ui.screens.detail_syllabus

import cafe.adriel.voyager.core.model.ScreenModel
import rise_front_end.team2.ui.Data.data_syllabus.Museum
import rise_front_end.team2.ui.Data.data_syllabus.MuseumRepository
import kotlinx.coroutines.flow.Flow

class DetailScreenModel(private val museumRepository: MuseumRepository) : ScreenModel {
    fun getObject(id: Int): Flow<Museum?> =
        museumRepository.getMuseumById(id)
}
