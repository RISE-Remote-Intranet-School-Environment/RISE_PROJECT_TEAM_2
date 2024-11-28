package rise_front_end.team2.ui.screens.syllabus.detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import rise_front_end.team2.Repo.SyllabusRepository
import rise_front_end.team2.data.syllabus.SyllabusObject

class SyllabusDetailViewModel(private val syllabusRepository: SyllabusRepository) : ViewModel() {
    fun getObject(objectId: Int): Flow<SyllabusObject?> =
        syllabusRepository.getObjectById(objectId)
}