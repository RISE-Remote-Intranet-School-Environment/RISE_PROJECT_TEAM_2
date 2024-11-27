package rise_front_end.team2


import android.app.Application
import rise_front_end.team2.di.initKoin

class SyllabusApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}