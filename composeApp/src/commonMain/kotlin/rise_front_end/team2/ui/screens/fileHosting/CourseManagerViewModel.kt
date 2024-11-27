package rise_front_end.team2.ui.screens.fileHosting


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import rise_front_end.team2.Repo.CourseManagerRepository
import rise_front_end.team2.data.courseManager.CourseObject

class CourseManagerViewModel(coursesRepository: CourseManagerRepository) : ViewModel() {
    val objects: StateFlow<List<CourseObject>> =
        coursesRepository.getObjects()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}