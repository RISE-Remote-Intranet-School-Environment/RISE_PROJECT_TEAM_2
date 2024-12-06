package rise_front_end.team2.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.data.data_profile.ProfileApi
import rise_front_end.team2.data.data_profile.ProfileObject
import rise_front_end.team2.data.data_profile.ProfileStorage


class ProfileRepository(
    private val syllabusApi: ProfileApi,
    private val syllabusStorage: ProfileStorage,
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

    fun getObjects(): Flow<List<ProfileObject>> = syllabusStorage.getObjects()

    fun getObjectById(objectId: Int): Flow<ProfileObject?> = syllabusStorage.getObjectById(objectId)
}