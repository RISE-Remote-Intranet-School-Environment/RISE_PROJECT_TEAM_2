package rise_front_end.team2.data.favorites

import android.content.Context

class AndroidLocalFileWriter(private val context: Context): LocalFileWriter {
    override suspend fun writeFile(fileName: String, content: String) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
        }
    }
}