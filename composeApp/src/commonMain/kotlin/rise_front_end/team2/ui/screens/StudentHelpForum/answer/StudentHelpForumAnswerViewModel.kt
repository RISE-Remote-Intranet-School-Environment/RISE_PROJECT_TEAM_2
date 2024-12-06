package rise_front_end.team2.ui.screens.StudentHelpForum.answer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.data.studentHelp.forum.ForumMessage


class ForumMessageAnswersViewModel(
    private val repository: StudentHelpForumRepository
) : ViewModel() {
    fun getForumMessage(courseId: Int, messageId: Int): Flow<ForumMessage?> =
        repository.getForumMessageById(courseId, messageId)
}
