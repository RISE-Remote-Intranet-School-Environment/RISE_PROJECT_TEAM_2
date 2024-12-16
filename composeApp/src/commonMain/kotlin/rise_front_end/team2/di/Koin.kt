package rise_front_end.team2.di

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import rise_front_end.team2.Repo.GradesRepository
import rise_front_end.team2.Repo.ProfileRepository
import rise_front_end.team2.Repo.StudentHelpForumRepository
import rise_front_end.team2.Repo.SyllabusRepository
import rise_front_end.team2.Repo.FavoritesRepository

import rise_front_end.team2.data.data_grades.GradeStorage
import rise_front_end.team2.data.data_grades.GradesApi
import rise_front_end.team2.data.data_grades.InMemoryGradeStorage
import rise_front_end.team2.data.data_grades.KtorGradesApi
import rise_front_end.team2.data.data_profile.InMemoryProfileStorage
import rise_front_end.team2.data.data_profile.KtorProfileApi
import rise_front_end.team2.data.data_profile.ProfileApi
import rise_front_end.team2.data.data_profile.ProfileStorage
import rise_front_end.team2.data.favorites.AndroidFavoritesFileHandler
import rise_front_end.team2.data.favorites.AndroidLocalFileReader
import rise_front_end.team2.data.favorites.AndroidLocalFileWriter

import rise_front_end.team2.ui.screens.screens_grades.GradeViewModel
import rise_front_end.team2.ui.screens.screens_profil.ProfileViewModel



import rise_front_end.team2.data.studentHelp.forum.KtorStudentHelpForumApi
import rise_front_end.team2.data.syllabus.KtorSyllabusApi
import rise_front_end.team2.data.studentHelp.forum.StudentHelpForumApi
import rise_front_end.team2.data.syllabus.SyllabusApi
import rise_front_end.team2.data.studentHelp.forum.InMemoryStudentHelpForumStorage
import rise_front_end.team2.data.syllabus.InMemorySyllabusStorage
import rise_front_end.team2.data.favorites.FavoriteApi
import rise_front_end.team2.data.favorites.FavoriteStorage
import rise_front_end.team2.data.favorites.FavoritesFileHandler
import rise_front_end.team2.data.favorites.InMemoryFavoritesStorage
import rise_front_end.team2.data.favorites.KtorFavoriteApi
import rise_front_end.team2.data.favorites.LocalFavoriteApi
import rise_front_end.team2.data.favorites.LocalFileReader
import rise_front_end.team2.data.favorites.LocalFileWriter
import rise_front_end.team2.data.studentHelp.forum.StudentHelpForumStorage
import rise_front_end.team2.data.syllabus.SyllabusStorage
import rise_front_end.team2.ui.screens.syllabus.detail.SyllabusDetailViewModel
import rise_front_end.team2.ui.screens.syllabus.list.SyllabusListViewModel
import rise_front_end.team2.ui.screens.StudentHelpForum.courseslist.StudentHelpForumListViewModel
import rise_front_end.team2.ui.screens.StudentHelpForum.answer.ForumMessageAnswersViewModel
import rise_front_end.team2.ui.screens.StudentHelpForum.posts.StudentHelpForumDetailViewModel
import rise_front_end.team2.ui.screens.studentHelp.files.filesList.CourseFilesViewModel
import rise_front_end.team2.ui.screens.studentHelp.files.fileanswers.FileDiscussionsViewModel
import rise_front_end.team2.ui.screens.favorites.FavoritesViewModel


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

    // APIs
    single<SyllabusApi> { KtorSyllabusApi(get()) }
    single<StudentHelpForumApi> { KtorStudentHelpForumApi(get()) }
    single<GradesApi> { KtorGradesApi(get()) }
    single<FavoriteApi> { LocalFavoriteApi(get()) }
    single<GradesApi> { KtorGradesApi(get()) }
    single<ProfileApi> { KtorProfileApi(get()) }


    // Storages
    single<SyllabusStorage> { InMemorySyllabusStorage() }
    single<StudentHelpForumStorage> { InMemoryStudentHelpForumStorage() }
    single<GradeStorage> { InMemoryGradeStorage() }
    single<FavoriteStorage> { InMemoryFavoritesStorage() }
    single<ProfileStorage> { InMemoryProfileStorage() }


    // Repositories
    single { SyllabusRepository(get(), get()).apply { initialize() } }
    single { StudentHelpForumRepository(get(), get()).apply { initialize() } }
    single { GradesRepository(get(), get()).apply { initialize() } }
    single { FavoritesRepository(get(), get()).apply { initialize() } }
    single {
        GradesRepository(get(), get()).apply { initialize() }
    }
    single {
        ProfileRepository(get(), get()).apply { initialize() }
    }

    // Favorites File Handlers
    single<LocalFileReader> { AndroidLocalFileReader(get()) }
    single<LocalFileWriter> { AndroidLocalFileWriter(get()) }
    single<FavoritesFileHandler> { (coroutineScope: CoroutineScope) ->
        AndroidFavoritesFileHandler(get(), get(), coroutineScope, "favorites.json")
    }
}


val viewModelModule = module {
    // Factory for Grades-related view models
    factoryOf(::GradeViewModel)
    factoryOf(::ProfileViewModel)

    // Factory for Syllabus-related view models
    factoryOf(::SyllabusListViewModel)
    factoryOf(::SyllabusDetailViewModel)

    // Factory for Student Help Forum-related view models
    factoryOf(::StudentHelpForumListViewModel)
    factoryOf(::StudentHelpForumDetailViewModel)
    factoryOf(::ForumMessageAnswersViewModel)

    // Factory for StuHelp files related view models
    factoryOf(::CourseFilesViewModel)
    factoryOf(::FileDiscussionsViewModel)

    // Factory for Favorites-related view models
    factoryOf(::FavoritesViewModel)

}

fun initKoin(context: Context) {
    startKoin {
        androidContext(context)
        modules(
            dataModule,
            viewModelModule,
        )
    }
}