package rise_front_end.team2.data.favorites

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

interface  FavoriteApi{
    suspend fun getData(): List<FavoritesObject>
}

class KtorFavoriteApi(private val client: HttpClient): FavoriteApi {
    companion object {
        private const val API_URL =
            "https://raw.githubusercontent.com/RISE-Remote-Intranet-School-Environment/RISE_PROJECT_TEAM_2/refs/heads/Tanguy/composeApp/src/commonMain/kotlin/rise_front_end/team2/navigation/favorites.json"
    }

    override suspend fun getData(): List<FavoritesObject> {
        return try {
            val responseBody = client.get(API_URL).body<String>()
            val jsonData = Json.decodeFromString<List<Map<String, Int>>>(responseBody)

            jsonData.mapNotNull { entry ->
                val courseID = entry["courseID"] ?: return@mapNotNull null
                val fileID = entry["fileID"]

                if (fileID == 0) {
                    FavoritesCourseObject(courseID)
                } else if (fileID != null) {
                    FavoritesFileObject(courseID, fileID)
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}