package rise_front_end.team2.ui.screens.StudentHelpForum.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.data.studentHelp.forum.Course
import rise_front_end.team2.data.studentHelp.forum.ForumMessage

class StudentHelpForumDetailViewModel(
    private val repository: StudentHelpForumRepository
) : ViewModel() {

    private val _tags = MutableStateFlow<List<String>>(emptyList())
    val tags: StateFlow<List<String>> = _tags

    fun loadTagsForCourse(courseId: Int) {
        viewModelScope.launch {
            repository.getTagsForCourse(courseId).collect {
                _tags.value = it
            }
        }
    }

    fun getCourse(courseId: Int): Flow<Course?> =
        repository.getCourseById(courseId)

    fun addForumMessage(courseId: Int, message: ForumMessage) = viewModelScope.launch {
        repository.addForumMessage(courseId, message)
    }

}
