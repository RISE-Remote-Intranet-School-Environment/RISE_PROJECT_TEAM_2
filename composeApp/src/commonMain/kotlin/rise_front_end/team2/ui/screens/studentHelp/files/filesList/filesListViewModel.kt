package rise_front_end.team2.ui.screens.studentHelp.files.filesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.data.studentHelp.forum.Course
import rise_front_end.team2.data.studentHelp.forum.CourseFile

class CourseFilesViewModel(
    private val repository: StudentHelpForumRepository
) : ViewModel() {
    fun getCourse(courseId: Int): Flow<Course?> =
        repository.getCourseById(courseId)

    fun addToFavorites(courseID: Int, courseFileID: Int){

    }

    fun removeFromFavorites(courseID: Int, courseFileID: Int){

    }

    fun likeFile(courseId: Int, fileId: Int) {
        viewModelScope.launch {
            repository.likeFile(courseId, fileId)
        }
    }
    fun addNewFile(courseId: Int, fileName: String, fileUrl: String,) {
        viewModelScope.launch {
            repository.addNewFile(courseId, fileName, fileUrl)
        }
    }



}
