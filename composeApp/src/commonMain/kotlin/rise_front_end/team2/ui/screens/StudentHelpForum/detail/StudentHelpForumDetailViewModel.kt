package rise_front_end.team2.ui.screens.StudentHelpForum.detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.data.objects.StudentHelpForumObject

class StudentHelpForumDetailViewModel(private val museumRepository: StudentHelpForumRepository) : ViewModel() {
    fun getObject(objectId: Int): Flow<StudentHelpForumObject?> =
        museumRepository.getObjectById(objectId)
}