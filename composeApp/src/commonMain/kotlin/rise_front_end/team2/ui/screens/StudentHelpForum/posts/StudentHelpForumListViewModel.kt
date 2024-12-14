package rise_front_end.team2.ui.screens.StudentHelpForum.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.data.studentHelp.forum.Course
import rise_front_end.team2.data.studentHelp.forum.ForumMessage

class StudentHelpForumDetailViewModel(
    private val repository: StudentHelpForumRepository
) : ViewModel() {
    fun getCourse(courseId: Int): Flow<Course?> =
        repository.getCourseById(courseId)

    fun addForumMessage(courseId: Int, message: ForumMessage) = viewModelScope.launch {
        repository.addForumMessage(courseId, message)
    }

    //Might need to use it later, we'll see
    fun updateForumMessage(courseId: Int, messageId: Int, newContent: String) = viewModelScope.launch {
        repository.updateForumMessage(courseId, messageId, newContent)
    }
}
