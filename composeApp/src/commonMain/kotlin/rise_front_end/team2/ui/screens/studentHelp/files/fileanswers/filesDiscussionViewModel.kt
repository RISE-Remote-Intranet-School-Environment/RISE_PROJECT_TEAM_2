package rise_front_end.team2.ui.screens.studentHelp.files.fileanswers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.data.studentHelp.forum.CourseFile
import rise_front_end.team2.data.studentHelp.forum.FileMessage
import rise_front_end.team2.data.studentHelp.forum.ForumMessage

class FileDiscussionsViewModel(
    private val repository: StudentHelpForumRepository
) : ViewModel() {
    fun getFile(courseId: Int, fileId: Int): Flow<CourseFile?> =
        repository.getFileById(courseId, fileId)

    fun addToFavorites(courseID: Int, courseFileID: Int){

    }

    fun removeFromFavorites(courseID: Int, courseFileID: Int){

    }

    fun addFileAnswer(courseId: Int, fileId: Int, message: FileMessage) = viewModelScope.launch{
        repository.addFileAnswer(courseId, fileId, message)
    }

    fun addForumMessage(courseId: Int, message: ForumMessage) = viewModelScope.launch {
        repository.addForumMessage(courseId, message)
    }


}

