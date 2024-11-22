package rise_front_end.team2.Repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.data.objects.StudentHelpForumObject
import rise_front_end.team2.data.api.StudentHelpForumApi
import rise_front_end.team2.data.storage.StudentHelpForumStorage


class StudentHelpForumRepository(
    private val studentHelpForumApi: StudentHelpForumApi,
    private val studentHelpForumStorage: StudentHelpForumStorage,
) {
    private val scope = CoroutineScope(SupervisorJob())

    fun initialize() {
        scope.launch {
            refresh()
        }
    }

    suspend fun refresh() {
        studentHelpForumStorage.saveObjects(studentHelpForumApi.getData())
    }

    fun getObjects(): Flow<List<StudentHelpForumObject>> = studentHelpForumStorage.getObjects()

    fun getObjectById(objectId: Int): Flow<StudentHelpForumObject?> = studentHelpForumStorage.getObjectById(objectId)
}