package rise_front_end.team2.data.data_grades


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import rise_front_end.team2.data.syllabus.SyllabusApi
import rise_front_end.team2.data.syllabus.SyllabusObject

interface GradeStorage {
    suspend fun saveObjects(newObjects: List<GradesObject>)

    fun getObjectById(objectId: Int): Flow<GradesObject?>

    fun getObjects(): Flow<List<GradesObject>>
}

class InMemoryGradeStorage : GradeStorage {
    private val storedObjects = MutableStateFlow(emptyList<GradesObject>())

    override suspend fun saveObjects(newObjects: List<GradesObject>) {
        storedObjects.value = newObjects
    }

    override fun getObjectById(objectId: Int): Flow<GradesObject?> {
        return storedObjects.map { objects ->
            objects.find { it.objectID == objectId }
        }
    }

    override fun getObjects(): Flow<List<GradesObject>> = storedObjects
}

class KtorSyllabusApi(private val client: HttpClient) : SyllabusApi {
    companion object {
        private const val API_URL =
            "https://raw.githubusercontent.com/Kotlin/KMP-App-Template/main/list.json"
    }

    override suspend fun getData(): List<SyllabusObject> {
        return try {
            val responseBody = client.get(API_URL).body<String>()
            Json.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}