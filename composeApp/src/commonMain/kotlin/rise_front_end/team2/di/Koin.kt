package rise_front_end.team2.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.Repo.SyllabusRepository
import rise_front_end.team2.data.api.KtorStudentHelpForumApi
import rise_front_end.team2.data.api.KtorSyllabusApi
import rise_front_end.team2.data.api.StudentHelpForumApi
import rise_front_end.team2.data.api.SyllabusApi
import rise_front_end.team2.data.storage.InMemoryStudentHelpForumStorage
import rise_front_end.team2.data.storage.InMemorySyllabusStorage
import rise_front_end.team2.data.storage.StudentHelpForumStorage
import rise_front_end.team2.data.storage.SyllabusStorage
import rise_front_end.team2.ui.screens.syllabus.detail.SyllabusDetailViewModel
import rise_front_end.team2.ui.screens.syllabus.list.SyllabusListViewModel
import rise_front_end.team2.ui.screens.StudentHelpForum.detail.StudentHelpForumDetailViewModel
import rise_front_end.team2.ui.screens.StudentHelpForum.list.StudentHelpForumListViewModel
// Data module - Dependency registration for APIs, storages, and repositories
val dataModule = module {
    single {
        // HTTP Client with JSON content negotiation
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    // APIs
    single<SyllabusApi> { KtorSyllabusApi(get()) }
    single<StudentHelpForumApi> { KtorStudentHelpForumApi(get()) }

    // Storages
    single<SyllabusStorage> { InMemorySyllabusStorage() }
    single<StudentHelpForumStorage> { InMemoryStudentHelpForumStorage() }

    // Repositories
    single { SyllabusRepository(get(), get()).apply { initialize() } }
    single { StudentHelpForumRepository(get(), get()).apply { initialize() } }
}

// ViewModel module - Dependency registration for view models
val viewModelModule = module {
    // Factory for Syllabus-related view models
    factoryOf(::SyllabusListViewModel)
    factoryOf(::SyllabusDetailViewModel)

    // Factory for Student Help Forum-related view models
    factoryOf(::StudentHelpForumListViewModel)
    factoryOf(::StudentHelpForumDetailViewModel)
}

// Initialize Koin with all modules
fun initKoin() {
    startKoin {
        // Add all modules here
        modules(
            dataModule,
            viewModelModule,
        )
    }
}
