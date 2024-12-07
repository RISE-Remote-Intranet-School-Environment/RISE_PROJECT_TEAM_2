package rise_front_end.team2.data.favorites

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import rise_front_end.team2.data.studentHelp.forum.Course
import rise_front_end.team2.data.studentHelp.forum.CourseFile
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface FavoritesFileHandler{
    val favorites: StateFlow<List<FavoritesObject>>

    suspend fun loadFavorites()
    suspend fun addCourseToFavorites(course: Course)
    suspend fun addFileToFavorites(course: Course, courseFile: CourseFile)
    suspend fun isCourseFavorite(courseID: Int): Boolean
    suspend fun isFileFavorite(courseID: Int, fileID: Int): Boolean
    suspend fun removeCourseFromFavorites(courseID: Int)
    suspend fun removeFileFromFavorites(courseID: Int, fileID: Int)
}

class AndroidFavoritesFileHandler (
    private val fileReader: LocalFileReader,
    private val fileWriter: LocalFileWriter,
    private val coroutineScope: CoroutineScope,
    private val fileName: String
) : FavoritesFileHandler {

    private val _favorites = MutableStateFlow<List<FavoritesObject>>(emptyList())
    override val favorites: StateFlow<List<FavoritesObject>> get() = _favorites

    override suspend fun loadFavorites() {
        try {
            val fileContent = fileReader.readFile(fileName)
            val favoritesList = Json.decodeFromString<List<FavoritesObject>>(fileContent)
            _favorites.value = favoritesList
        } catch (e: Exception) {
            e.printStackTrace()
            _favorites.value = emptyList()
        }
    }

    private fun saveFavorites() {
        coroutineScope.launch {
            try {
                val jsonContent = Json.encodeToString(_favorites.value)
                fileWriter.writeFile(fileName, jsonContent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun addCourseToFavorites(course: Course) {
        if (!isCourseFavorite(course.courseID)) {
            val newFavorite = FavoritesCourseObject(
                courseID = course.courseID,
                courseName = course.courseName
            )
            _favorites.value += newFavorite
            saveFavorites()
        }
    }

    override suspend fun addFileToFavorites(course: Course, courseFile: CourseFile) {
        if (!isFileFavorite(course.courseID, courseFile.fileID)) {
            val newFavorite = FavoritesFileObject(
                courseID = course.courseID,
                courseName = course.courseName,
                fileID = courseFile.fileID,
                fileName = courseFile.fileName
            )
            _favorites.value += newFavorite
            saveFavorites()
        }
    }

    override suspend fun isCourseFavorite(courseID: Int): Boolean {
        return _favorites.value.any { it is FavoritesCourseObject && it.courseID == courseID }
    }

    override suspend fun isFileFavorite(courseID: Int, fileID: Int): Boolean {
        return _favorites.value.any {
            it is FavoritesFileObject && it.courseID == courseID && it.fileID == fileID
        }
    }

    override suspend fun removeCourseFromFavorites(courseID: Int) {
        val updatedFavorites = _favorites.value.filterNot {
            it is FavoritesCourseObject && it.courseID == courseID
        }
        _favorites.value = updatedFavorites
        saveFavorites()
    }

    override suspend fun removeFileFromFavorites(courseID: Int, fileID: Int) {
        val updatedFavorites = _favorites.value.filterNot {
            it is FavoritesFileObject && it.courseID == courseID && it.fileID == fileID
        }
        _favorites.value = updatedFavorites
        saveFavorites()
    }
}