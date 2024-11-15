package rise_front_end.team2.data.FileManager

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface FileManagerStorage {
    suspend fun saveObjects(newObjects: List<FileManagerObject>)

    fun getObjectById(objectId: Int): Flow<FileManagerObject?>

    fun getObjects(): Flow<List<FileManagerObject>>
}

class InMemoryFileManagerStorage : FileManagerStorage {
    private val storedObjects = MutableStateFlow(emptyList<FileManagerObject>())

    override suspend fun saveObjects(newObjects: List<FileManagerObject>) {
        storedObjects.value = newObjects
    }

    override fun getObjectById(objectId: Int): Flow<FileManagerObject?> {
        return storedObjects.map { objects ->
            objects.find { it.objectID == objectId }
        }
    }

    override fun getObjects(): Flow<List<FileManagerObject>> = storedObjects
}