package rise_front_end.team2.ui.screens.screens_grades
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import rise_front_end.team2.Repo.GradesRepository
import rise_front_end.team2.data.data_grades.GradesObject

class GradeViewModel(gradesRepository: GradesRepository) : ViewModel() {
    val objects: StateFlow<List<GradesObject>> =
        gradesRepository.getObjects()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}