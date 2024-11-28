package rise_front_end.team2.ui.screens.StudentHelpForum.list


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import rise_front_end.team2.data.studentHelp.forum.StudentHelpForumObject
import rise_front_end.team2.Repo.StudentHelpForumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class StudentHelpForumListViewModel(StudentHelpForumRepository: StudentHelpForumRepository) : ViewModel() {
    val objects: StateFlow<List<StudentHelpForumObject>> =
        StudentHelpForumRepository.getObjects()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}