package com.rise_front_end.team2.ui.Di.di_syllabus

import rise_front_end.team2.ui.Data.data_syllabus.InMemoryMuseumStorage
import rise_front_end.team2.ui.Data.data_syllabus.KtorMuseumApi
import rise_front_end.team2.ui.Data.data_syllabus.MuseumApi
import rise_front_end.team2.ui.Data.data_syllabus.MuseumRepository
import rise_front_end.team2.ui.Data.data_syllabus.MuseumStorage
import rise_front_end.team2.ui.screens.detail_syllabus.DetailScreenModel
import rise_front_end.team2.ui.screens.list_syllabus.ListScreenModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

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

    single<MuseumApi> { KtorMuseumApi(get()) }
    single<MuseumStorage> { InMemoryMuseumStorage() }
    single {
        MuseumRepository(get(), get()).apply {
            initialize()
        }
    }
}

val screenModelsModule = module {
    factoryOf(::ListScreenModel)
    factoryOf(::DetailScreenModel)
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            screenModelsModule,
        )
    }
}
