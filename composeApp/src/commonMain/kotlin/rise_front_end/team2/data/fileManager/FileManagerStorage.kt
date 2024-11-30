package rise_front_end.team2.data.fileManager

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface FileManagerStorage {
    suspend fun saveObjects(newObjects: List<FileObject>)

    fun getObjectById(fileID: String): Flow<FileObject?>

    fun getObjects(): Flow<List<FileObject>>
}

class InMemoryFileManagerStorage : FileManagerStorage {
    private val storedObjects = MutableStateFlow(emptyList<FileObject>())

    override suspend fun saveObjects(newObjects: List<FileObject>) {
        storedObjects.value = newObjects
    }

    override fun getObjectById(fileID: String): Flow<FileObject?> {
        return storedObjects.map { objects ->
            objects.find { it.fileID == fileID }
        }
    }

    override fun getObjects(): Flow<List<FileObject>> = storedObjects
}