package rise_front_end.team2.data.courseManager

import android.content.Context
import kotlinx.serialization.json.Json

interface CourseManagerApi {
    suspend fun getData(): List<CourseObject>
}

class LocalCourseManagerApi(private val context: Context) : CourseManagerApi {
    override suspend fun getData(): List<CourseObject> {
        return try {
            val jsonString = context.assets.open("Courses.json").bufferedReader().use { it.readText() }
            Json.decodeFromString(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

//class KtorCourseManagerApi(private val client: HttpClient) : CourseManagerApi {
//    companion object {
//        private const val API_URL =
//            "https://raw.githubusercontent.com/Kotlin/KMP-App-Template/main/list.json"
//    }
//
//    override suspend fun getData(): List<CourseObject> {
//        return try {
//            val responseBody = client.get(API_URL).body<String>()
//            Json.decodeFromString(responseBody)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList()
//        }
//    }
//}