package rise_front_end.team2.data.data_grades



import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

interface GradesApi {
    suspend fun getData(): List<GradesObject>
}


class KtorGradesApi(private val client: HttpClient) : GradesApi {
    companion object {
        private const val API_URL =
            "https://raw.githubusercontent.com/RISE-Remote-Intranet-School-Environment/RISE_PROJECT_TEAM_2/refs/heads/master/composeApp/src/commonMain/kotlin/rise_front_end/team2/data/data_grades/grades.json"
    }

    override suspend fun getData(): List<GradesObject> {
        return try {
            val responseBody = client.get(API_URL).body<String>()
            Json.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}