package rise_front_end.team2

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import rise_front_end.team2.di.dataModule
import rise_front_end.team2.di.viewModelModule

import rise_front_end.team2.Repo.FavoritesRepository
import rise_front_end.team2.data.favorites.*
import rise_front_end.team2.ui.screens.favorites.FavoritesViewModel

// Android-specific Koin module to handle platform-dependent services
val androidModule = module {
    // Provide Android-specific context-based file reader
    single<LocalFileReader> { AndroidLocalFileReader(get()) }
    single<LocalFileWriter> { AndroidLocalFileWriter(get()) }

    single<FavoriteApi> { LocalFavoriteApi(get()) }

    single<FavoritesFileHandler> { (coroutineScope: CoroutineScope) ->
        AndroidFavoritesFileHandler(get(), get(), coroutineScope, "favorites.json")
    }
//    single<FavoriteStorage> { InMemoryFavoritesStorage() }
//    single { FavoritesRepository(get(), get()).apply { initialize() } }
}

//val androidViewModelModule = module {
//    factory { (favoritesRepository: FavoritesRepository, favoritesFileHandler: FavoritesFileHandler) ->
//        FavoritesViewModel(favoritesRepository, favoritesFileHandler, CoroutineScope(Dispatchers.IO))
//    }
//}

fun initAndroidKoin(context: Context) {
    startKoin {
        androidContext(context) // Pass the Android context to Koin
        modules(
            dataModule,       // Common data module
            viewModelModule,  // Common ViewModel module
            androidModule,     // Android-specific module
//            androidViewModelModule
        )
    }
}