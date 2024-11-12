package rise_front_end.team2.di


import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import rise_front_end.team2.Repo.SyllabusRepository
import rise_front_end.team2.data.InMemorySyllabusStorage
import rise_front_end.team2.data.KtorSyllabusApi
import rise_front_end.team2.data.SyllabusApi
import rise_front_end.team2.data.SyllabusStorage


import rise_front_end.team2.ui.screens.detail.DetailViewModel
import rise_front_end.team2.ui.screens.list.ListViewModel



val dataModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }
        }
    }

    single<SyllabusApi> { KtorSyllabusApi(get()) }
    single<SyllabusStorage> { InMemorySyllabusStorage() }
    single {
        SyllabusRepository(get(), get()).apply {
            initialize()
        }
    }
}

val viewModelModule = module {
    factoryOf(::ListViewModel)
    factoryOf(::DetailViewModel)
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            viewModelModule,
        )
    }
}