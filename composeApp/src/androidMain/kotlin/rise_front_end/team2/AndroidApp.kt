package rise_front_end.team2

import android.app.Application
// import org.koin.android.ext.koin.androidContext
import rise_front_end.team2.di.initKoin

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initAndroidKoin(this)
    }
}