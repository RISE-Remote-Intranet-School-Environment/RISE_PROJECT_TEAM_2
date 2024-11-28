package rise_front_end.team2.di


import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import rise_front_end.team2.Repo.GradesRepository
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.Repo.SyllabusRepository
import rise_front_end.team2.data.InMemorySyllabusStorage
import rise_front_end.team2.data.KtorSyllabusApi
import rise_front_end.team2.data.SyllabusApi
import rise_front_end.team2.data.SyllabusStorage
import rise_front_end.team2.data.data_grades.GradeStorage
import rise_front_end.team2.data.data_grades.GradesApi
import rise_front_end.team2.data.data_grades.InMemoryGradeStorage
import rise_front_end.team2.data.data_grades.KtorGradesApi


import rise_front_end.team2.ui.screens.detail.DetailViewModel
import rise_front_end.team2.ui.screens.list.ListViewModel

import rise_front_end.team2.ui.screens.screens_grades.GradeViewModel



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
        // Initialize Ktor HTTP Client with Content Negotiation for JSON serialization
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }) // Handle unknown keys in the JSON response
            }
        }
    }

    // Ensure the correct SyllabusApi implementation is injected
    single<SyllabusApi> { KtorSyllabusApi(get()) }
    single<StudentHelpForumApi> { KtorStudentHelpForumApi(get()) }


    // Storages
    single<SyllabusStorage> { InMemorySyllabusStorage() }
    single<StudentHelpForumStorage> { InMemoryStudentHelpForumStorage() }
    single<GradesApi> { KtorGradesApi(get()) }
    single<GradeStorage> { InMemoryGradeStorage() }


    // Repositories
    single { SyllabusRepository(get(), get()).apply { initialize() } }
    single { StudentHelpForumRepository(get(), get()).apply { initialize() } }
    single {
        GradesRepository(get(), get()).apply { initialize() }
    }
}


val viewModelModule = module {
    factoryOf(::GradeViewModel)
    // Factory for Syllabus-related view models
    factoryOf(::SyllabusListViewModel)
    factoryOf(::SyllabusDetailViewModel)

    // Factory for Student Help Forum-related view models
    factoryOf(::StudentHelpForumListViewModel)
    factoryOf(::StudentHelpForumDetailViewModel)
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            viewModelModule,
        )
    }
}