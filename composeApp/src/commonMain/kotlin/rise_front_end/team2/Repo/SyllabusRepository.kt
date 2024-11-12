package rise_front_end.team2.Repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.data.SyllabusObject
import rise_front_end.team2.data.SyllabusApi
import rise_front_end.team2.data.SyllabusStorage


class SyllabusRepository(
    private val syllabusApi: SyllabusApi,
    private val syllabusStorage: SyllabusStorage,
) {
    private val scope = CoroutineScope(SupervisorJob())

    fun initialize() {
        scope.launch {
            refresh()
        }
    }

    suspend fun refresh() {
        syllabusStorage.saveObjects(syllabusApi.getData())
    }

    fun getObjects(): Flow<List<SyllabusObject>> = syllabusStorage.getObjects()

    fun getObjectById(objectId: Int): Flow<SyllabusObject?> = syllabusStorage.getObjectById(objectId)
}