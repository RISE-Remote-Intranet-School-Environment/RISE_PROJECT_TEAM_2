package rise_front_end.team2.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import rise_front_end.team2.data.objects.StudentHelpForumObject

interface StudentHelpForumApi {
    suspend fun getData(): List<StudentHelpForumObject>
}

class KtorStudentHelpForumApi(private val client: HttpClient) : StudentHelpForumApi {
    companion object {
        private const val API_URL =
            "https://raw.githubusercontent.com/Kotlin/KMP-App-Template/main/list.json"
    }

    override suspend fun getData(): List<StudentHelpForumObject> {
        return try {
            val responseBody = client.get(API_URL).body<String>()
            Json.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}