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
            "https://github.com/RISE-Remote-Intranet-School-Environment/RISE_PROJECT_TEAM_2/tree/Tanguy/composeApp/src/commonMain/kotlin/rise_front_end/team2/navigation/favorites.json"
    }

    override suspend fun getData(): List<FavoritesObject> {
        return try {
            val responseBody = client.get(API_URL).body<String>()
            Json.decodeFromString(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}