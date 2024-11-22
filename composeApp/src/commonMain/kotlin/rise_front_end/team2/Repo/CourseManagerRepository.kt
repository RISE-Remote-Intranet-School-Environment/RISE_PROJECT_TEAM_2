package rise_front_end.team2.Repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.data.CourseManager.CourseManagerApi
import rise_front_end.team2.data.CourseManager.CourseObject
import rise_front_end.team2.data.CourseManager.CourseManagerStorage



class CourseManagerRepository (
    private val courseManagerApi: CourseManagerApi,
    private val courseManagerStorage: CourseManagerStorage, )
{
    private val scope = CoroutineScope(SupervisorJob())

    fun initialize() {
        scope.launch {
            refresh()
        }
    }

    suspend fun refresh() {
        courseManagerStorage.saveObjects(courseManagerApi.getData())
    }

    fun getObjects(): Flow<List<CourseObject>> = courseManagerStorage.getObjects()

    fun getObjectById(objectId: String): Flow<CourseObject?> = courseManagerStorage.getObjectById(objectId)
}