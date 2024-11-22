package rise_front_end.team2.Repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import rise_front_end.team2.data.FileManager.FileObject
import rise_front_end.team2.data.FileManager.FileManagerApi
import rise_front_end.team2.data.FileManager.FileManagerStorage


class FileManagerRepository(
    private val fileManagerApi: FileManagerApi,
    private val fileManagerStorage: FileManagerStorage,
) {
    private val scope = CoroutineScope(SupervisorJob())

    fun initialize() {
        scope.launch {
            refresh()
        }
    }

    suspend fun refresh() {
        fileManagerStorage.saveObjects(fileManagerApi.getData())
    }

    fun getObjects(): Flow<List<FileObject>> = fileManagerStorage.getObjects()

    fun getObjectById(objectId: String): Flow<FileObject?> = fileManagerStorage.getObjectById(objectId)
}