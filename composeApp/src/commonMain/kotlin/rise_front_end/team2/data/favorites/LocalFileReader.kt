package rise_front_end.team2.data.favorites

interface LocalFileReader {
    suspend fun readFile(fileName: String): String
}