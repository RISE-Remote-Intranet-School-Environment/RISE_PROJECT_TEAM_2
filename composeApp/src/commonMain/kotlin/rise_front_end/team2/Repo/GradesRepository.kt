package rise_front_end.team2.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.data.data_grades.GradesApi
import rise_front_end.team2.data.data_grades.GradeStorage
import rise_front_end.team2.data.data_grades.GradesObject


class GradesRepository(
    private val syllabusApi: GradesApi,
    private val syllabusStorage: GradeStorage,
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

    fun getObjects(): Flow<List<GradesObject>> = syllabusStorage.getObjects()

    fun getObjectById(objectId: Int): Flow<GradesObject?> = syllabusStorage.getObjectById(objectId)
}