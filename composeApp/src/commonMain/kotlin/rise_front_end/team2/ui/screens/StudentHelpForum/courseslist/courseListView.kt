package rise_front_end.team2.ui.screens.StudentHelpForum.courseslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.data.studentHelp.forum.Course

class StudentHelpForumListViewModel(
    private val repository: StudentHelpForumRepository
) : ViewModel() {
    val courses: StateFlow<List<Course>> =
        repository.getCourses()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
