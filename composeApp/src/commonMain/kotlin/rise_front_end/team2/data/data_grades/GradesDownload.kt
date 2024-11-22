package rise_front_end.team2.data.data_grades
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

fun downloadFile(context: Context, fileName: String) {
    val request = DownloadManager.Request(Uri.parse("https://raw.githubusercontent.com/Warzieram/rise_project_json/refs/heads/main/grades.json")).apply {
        setTitle("Téléchargement de $fileName")
        setDescription("Téléchargement en cours...")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
    }

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
}
