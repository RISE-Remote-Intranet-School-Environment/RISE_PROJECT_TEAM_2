package rise_front_end.team2.ui.screens.studentHelp.files.fileanswers

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.data.studentHelp.forum.CourseFile

class FileDiscussionsViewModel(
    private val repository: StudentHelpForumRepository
) : ViewModel() {
    fun getFile(courseId: Int, fileId: Int): Flow<CourseFile?> =
        repository.getFileById(courseId, fileId)
}

