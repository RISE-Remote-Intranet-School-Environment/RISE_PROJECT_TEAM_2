package rise_front_end.team2.ui.screens.StudentHelpForum.posts


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.data.studentHelp.forum.Course

class StudentHelpForumDetailViewModel(
    private val repository: StudentHelpForumRepository
) : ViewModel() {
    fun getCourse(courseId: Int): Flow<Course?> =
        repository.getCourseById(courseId)
}
