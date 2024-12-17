package rise_front_end.team2.ui.screens.StudentHelpForum.answer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.data.studentHelp.forum.Answer
import rise_front_end.team2.data.studentHelp.forum.ForumMessage


class ForumMessageAnswersViewModel(
    private val repository: StudentHelpForumRepository
) : ViewModel() {
    fun getForumMessage(courseId: Int, messageId: Int): Flow<ForumMessage?> =
        repository.getForumMessageById(courseId, messageId)

    fun addAnswer(courseId: Int, messageId: Int, answer: Answer) = viewModelScope.launch {
        repository.addAnswer(courseId, messageId, answer)
    }

    fun likeAnswer(courseId: Int, messageId: Int, answerId: Int) = viewModelScope.launch {
        repository.likeAnswer(courseId, messageId, answerId)
    }
}

