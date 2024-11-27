package rise_front_end.team2.di

import android.app.Application
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import rise_front_end.team2.Repo.CourseManagerRepository
import rise_front_end.team2.Repo.FileManagerRepository
import rise_front_end.team2.Repo.SyllabusRepository
import rise_front_end.team2.data.courseManager.CourseManagerApi
import rise_front_end.team2.data.courseManager.CourseManagerStorage
import rise_front_end.team2.data.courseManager.InMemoryCourseManagerStorage
import rise_front_end.team2.data.courseManager.LocalCourseManagerApi
import rise_front_end.team2.data.InMemorySyllabusStorage
import rise_front_end.team2.data.KtorSyllabusApi
import rise_front_end.team2.data.SyllabusApi
import rise_front_end.team2.data.SyllabusStorage


import rise_front_end.team2.ui.screens.detail.DetailViewModel
import rise_front_end.team2.ui.screens.list.ListViewModel
import rise_front_end.team2.ui.screens.fileHosting.CourseManagerViewModel



val dataModule = module {
    single {
        // Initialize Ktor HTTP Client with Content Negotiation for JSON serialization
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }) // Handle unknown keys in the JSON response
            }
        }
    }

    // Ensure the correct SyllabusApi implementation is injected
    single<SyllabusApi> { KtorSyllabusApi(get()) }
    single<SyllabusStorage> { InMemorySyllabusStorage() }

    // Syllabus repository with initialization
    single {
        SyllabusRepository(get(), get()).apply { initialize() }
    }

    single<LocalCourseManagerApi> { LocalCourseManagerApi(androidContext()) }
    single<CourseManagerStorage> { InMemoryCourseManagerStorage() }
    single{
        CourseManagerRepository(get(), get()).apply { initialize() }
    }

//    single<FileManagerApi> { KtorFileManagerApi(get()) }
//    single<FileManagerStorage> { InMemoryFileManagerStorage() }
//    single{
//        FileManagerRepository(get(), get()).apply { initialize() }
//    }
}


val viewModelModule = module {
    factoryOf(::ListViewModel)
    factoryOf(::DetailViewModel)
    factoryOf(::CourseManagerViewModel)
}

fun initKoin(application: Application) {
    startKoin {
        androidContext(application) // Provide the Android context
        modules(
            dataModule,
            viewModelModule,
        )
    }
}