package rise_front_end.team2.Repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.data.courseManager.LocalCourseManagerApi
import rise_front_end.team2.data.courseManager.CourseObject
import rise_front_end.team2.data.courseManager.CourseManagerStorage



class CourseManagerRepository (
    private val courseManagerApi: LocalCourseManagerApi,
    private val courseManagerStorage: CourseManagerStorage, )
{
    private val scope = CoroutineScope(SupervisorJob())

    fun initialize() {
        scope.launch {
            refresh()
        }
    }

    suspend fun refresh() {
        try {
            val data = courseManagerApi.getData()
            courseManagerStorage.saveObjects(data)
        } catch (e: Exception) {
            // Log or handle error
            e.printStackTrace()
        }
    }

    fun getObjects(): Flow<List<CourseObject>> = courseManagerStorage.getObjects()

    fun getObjectById(objectId: String): Flow<CourseObject?> = courseManagerStorage.getObjectById(objectId)
}