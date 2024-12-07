package rise_front_end.team2.data.favorites

interface LocalFileWriter {
    suspend fun writeFile(fileName : String, content : String)
}