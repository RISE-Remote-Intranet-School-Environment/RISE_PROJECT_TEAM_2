package rise_front_end.team2.data.data_profile


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json


interface ProfileStorage {
    suspend fun saveObjects(newObjects: List<ProfileObject>)

    fun getObjectById(objectId: Int): Flow<ProfileObject?>

    fun getObjects(): Flow<List<ProfileObject>>
}

class InMemoryProfileStorage : ProfileStorage {
    private val storedObjects = MutableStateFlow(emptyList<ProfileObject>())

    override suspend fun saveObjects(newObjects: List<ProfileObject>) {
        storedObjects.value = newObjects
    }

    override fun getObjectById(objectId: Int): Flow<ProfileObject?> {
        return storedObjects.map { objects ->
            objects.find { it.objectID == objectId }
        }
    }

    override fun getObjects(): Flow<List<ProfileObject>> = storedObjects
}

class KtorSyllabusApi(private val client: HttpClient) : ProfileApi {
    companion object {
        private const val API_URL =
            "https://raw.githubusercontent.com/Kotlin/KMP-App-Template/main/list.json"
    }

    override suspend fun getData(): List<ProfileObject> {
        return try {
            val responseBody = client.get(API_URL).body<String>()
            Json.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}