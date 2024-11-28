package rise_front_end.team2.data.studentHelp.forum

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface StudentHelpForumStorage {
    suspend fun saveObjects(newObjects: List<StudentHelpForumObject>)

    fun getObjectById(objectId: Int): Flow<StudentHelpForumObject?>

    fun getObjects(): Flow<List<StudentHelpForumObject>>
}

class InMemoryStudentHelpForumStorage : StudentHelpForumStorage {
    private val storedObjects = MutableStateFlow(emptyList<StudentHelpForumObject>())

    override suspend fun saveObjects(newObjects: List<StudentHelpForumObject>) {
        storedObjects.value = newObjects
    }

    override fun getObjectById(objectId: Int): Flow<StudentHelpForumObject?> {
        return storedObjects.map { objects ->
            objects.find { it.objectID == objectId }
        }
    }

    override fun getObjects(): Flow<List<StudentHelpForumObject>> = storedObjects
}