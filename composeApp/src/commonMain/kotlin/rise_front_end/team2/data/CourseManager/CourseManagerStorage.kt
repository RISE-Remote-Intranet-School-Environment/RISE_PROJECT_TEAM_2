package rise_front_end.team2.data.CourseManager

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface CourseManagerStorage {
    suspend fun saveObjects(newObjects: List<CourseObject>)

    fun getObjectById(courseID: String): Flow<CourseObject?>

    fun getObjects(): Flow<List<CourseObject>>
}

class InMemoryCourseManagerStorage : CourseManagerStorage {
    private val storedObjects = MutableStateFlow(emptyList<CourseObject>())

    override suspend fun saveObjects(newObjects: List<CourseObject>) {
        storedObjects.value = newObjects
    }

    override fun getObjectById(courseID: String): Flow<CourseObject?> {
        return storedObjects.map { objects ->
            objects.find { it.courseObjectID == courseID }
        }
    }

    override fun getObjects(): Flow<List<CourseObject>> = storedObjects
}