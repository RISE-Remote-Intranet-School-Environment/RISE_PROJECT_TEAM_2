package rise_front_end.team2.data.studentHelp.forum

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

interface StudentHelpForumApi {
    suspend fun getData(): List<Course>
}

class KtorStudentHelpForumApi(private val client: HttpClient) : StudentHelpForumApi {
    companion object {
        private const val API_URL =
            "https://raw.githubusercontent.com/RISE-Remote-Intranet-School-Environment/RISE_PROJECT_TEAM_2/refs/heads/Tanguy/composeApp/src/commonMain/kotlin/rise_front_end/team2/data/studentHelp/forum/studentHelpForum.json"
    }

    override suspend fun getData(): List<Course> {
        return try {
            val responseBody = client.get(API_URL).body<String>()
            Json.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

