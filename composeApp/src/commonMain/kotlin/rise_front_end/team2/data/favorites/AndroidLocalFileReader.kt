package rise_front_end.team2.data.favorites

import android.content.Context

class AndroidLocalFileReader(private val context: Context) : LocalFileReader {
    override suspend fun readFile(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}