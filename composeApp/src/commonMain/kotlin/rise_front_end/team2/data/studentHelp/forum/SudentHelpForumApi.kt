package rise_front_end.team2.data.studentHelp.forum

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

interface StudentHelpForumApi {
    suspend fun getData(): List<Course>
}

class KtorStudentHelpForumApi(private val context: Context) : StudentHelpForumApi {
    override suspend fun getData(): List<Course> {
        return try {
            // Open the JSON file from assets
            val inputStream = context.assets.open("studentHelpForum.json")

            // Read the file contents
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            // Parse the JSON string into a list of Course objects
            Json {
                ignoreUnknownKeys = true // Helpful for handling additional JSON fields
            }.decodeFromString(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}