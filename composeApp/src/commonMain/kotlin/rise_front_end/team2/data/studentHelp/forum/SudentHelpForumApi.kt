package rise_front_end.team2.data.studentHelp.forum

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

interface StudentHelpForumApi {
    suspend fun getData(): List<Course>
}

class KtorStudentHelpForumApi(private val context: Context) : StudentHelpForumApi {
    companion object {
        private val json = Json { ignoreUnknownKeys = true }
    }

    override suspend fun getData(): List<Course> {
        return try {
            val inputStream = context.assets.open("studentHelpForum.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            json.decodeFromString(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}