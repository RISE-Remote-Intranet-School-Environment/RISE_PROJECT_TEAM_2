package rise_front_end.team2.data.favorites

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.*

interface FavoriteApi {
    suspend fun getData(): List<FavoritesObject>
}

class KtorFavoriteApi(private val client: HttpClient) : FavoriteApi {
    companion object {
        private const val API_URL =
            "https://raw.githubusercontent.com/RISE-Remote-Intranet-School-Environment/RISE_PROJECT_TEAM_2/refs/heads/Tanguy/composeApp/src/commonMain/kotlin/rise_front_end/team2/navigation/favorites.json"
    }

    override suspend fun getData(): List<FavoritesObject> {
        return try {
            val responseBody = client.get(API_URL).body<String>()
            parseFavoritesJson(responseBody)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

class LocalFavoriteApi(private val fileReader: LocalFileReader) : FavoriteApi {
    override suspend fun getData(): List<FavoritesObject> {
        return try {
            val fileContent = fileReader.readFile("favorites.json")
            parseFavoritesJson(fileContent)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

private fun parseFavoritesJson(jsonString: String): List<FavoritesObject> {
    return try {
        val jsonArray = Json.parseToJsonElement(jsonString).jsonArray

        jsonArray.mapNotNull { element ->
            val jsonObject = element.jsonObject

            val courseID = jsonObject["courseID"]?.jsonPrimitive?.intOrNull
            val courseName = jsonObject["courseName"]?.jsonPrimitive?.contentOrNull
            val fileID = jsonObject["fileID"]?.jsonPrimitive?.intOrNull
            val fileName = jsonObject["fileName"]?.jsonPrimitive?.contentOrNull

            when {
                courseID != null && fileID == null && courseName != null -> {
                    FavoritesCourseObject(courseID, courseName)
                }
                courseID != null && fileID != null && courseName != null && fileName != null -> {
                    FavoritesFileObject(courseID, courseName, fileID, fileName)
                }
                else -> null
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
