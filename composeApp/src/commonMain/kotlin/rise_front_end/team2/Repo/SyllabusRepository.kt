package rise_front_end.team2.Repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.data.syllabus.SyllabusObject
import rise_front_end.team2.data.syllabus.SyllabusApi
import rise_front_end.team2.data.syllabus.SyllabusStorage


class SyllabusRepository(
    private val studentHelpForumApi: SyllabusApi,
    private val studentHelpForumStorage: SyllabusStorage,
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

    fun getObjects(): Flow<List<SyllabusObject>> = studentHelpForumStorage.getObjects()

    fun getObjectById(objectId: Int): Flow<SyllabusObject?> = studentHelpForumStorage.getObjectById(objectId)
}