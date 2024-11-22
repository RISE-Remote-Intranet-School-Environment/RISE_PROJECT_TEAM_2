package rise_front_end.team2.data.storage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import rise_front_end.team2.data.objects.SyllabusObject

interface SyllabusStorage {
    suspend fun saveObjects(newObjects: List<SyllabusObject>)

    fun getObjectById(objectId: Int): Flow<SyllabusObject?>

    fun getObjects(): Flow<List<SyllabusObject>>
}

class InMemorySyllabusStorage : SyllabusStorage {
    private val storedObjects = MutableStateFlow(emptyList<SyllabusObject>())

    override suspend fun saveObjects(newObjects: List<SyllabusObject>) {
        storedObjects.value = newObjects
    }

    override fun getObjectById(objectId: Int): Flow<SyllabusObject?> {
        return storedObjects.map { objects ->
            objects.find { it.objectID == objectId }
        }
    }

    override fun getObjects(): Flow<List<SyllabusObject>> = storedObjects
}