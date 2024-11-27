package rise_front_end.team2.ui.screens.detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import rise_front_end.team2.Repo.SyllabusRepository
import rise_front_end.team2.data.SyllabusObject

class DetailViewModel(private val museumRepository: SyllabusRepository) : ViewModel() {
    fun getObject(objectId: Int): Flow<SyllabusObject?> =
        museumRepository.getObjectById(objectId)
}