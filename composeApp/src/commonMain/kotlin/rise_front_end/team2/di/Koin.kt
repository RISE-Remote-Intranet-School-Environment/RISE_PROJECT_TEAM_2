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
import rise_front_end.team2.Repo.FileManagerRepository
import rise_front_end.team2.Repo.SyllabusRepository
import rise_front_end.team2.data.FileManager.FileManagerApi
import rise_front_end.team2.data.FileManager.FileManagerStorage
import rise_front_end.team2.data.FileManager.InMemoryFileManagerStorage
import rise_front_end.team2.data.FileManager.KtorFileManagerApi
import rise_front_end.team2.data.InMemorySyllabusStorage
import rise_front_end.team2.data.KtorSyllabusApi
import rise_front_end.team2.data.SyllabusApi
import rise_front_end.team2.data.SyllabusStorage


import rise_front_end.team2.ui.screens.detail.DetailViewModel
import rise_front_end.team2.ui.screens.list.ListViewModel
import rise_front_end.team2.ui.screens.fileHosting.FileHostingViewModel



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

    single<FileManagerApi> { KtorFileManagerApi(get()) }
    single<FileManagerStorage> { InMemoryFileManagerStorage() }
    single{
        FileManagerRepository(get(), get()).apply { initialize() }
    }
}


val viewModelModule = module {
    factoryOf(::ListViewModel)
    factoryOf(::DetailViewModel)
    factoryOf(::FileHostingViewModel)
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            viewModelModule,
        )
    }
}