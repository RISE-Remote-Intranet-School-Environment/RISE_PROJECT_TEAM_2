# **Kotlin Compose Multiplatform - School App**

## **Description**

This project is a **Kotlin Compose Multiplatform** application designed for visualizing all of the information about your scholarship in one place.

---

## **Features**

1. **Calendar Page**

2. **Syllabus Page**

3. **Grades Page**
  - **Location**: `rise_front_end.team2.ui.screens.screens_grades`
  - **Functionality**:
    - Displays grades and calculates validated ECTS credits.
    - Dynamic search bar for filtering results.
    - Animated expandable content showing GPA and jury decision.
    - Allows users to register for exams individually or collectively.
  - **Architecture**:
    - **GradeViewModel.kt** : Retrieves grade objects via a `StateFlow` using `GradesRepository`.
    - **GradeScreen.kt** : Display grades data.
    - **RegistrationScreen.kt** : Allows users to register for exams individually or collectively.


4. **Profile And Shop Pages**
  - **Location**: `rise_front_end.team2.ui.screens.screens_profil`
  - **Functionality**:
    - Displays user information (Class, Option, Year).
    - Displays cumulative points for the shop.
    - Toggle switch for light/dark theme.
    - Access to the shop screen via a button.
    - Switch between light and dark mode using a toggle button.
  - **Architecture**:
    - **ProfileViewModel.kt**: Fetches profile data using `ProfileRepository`.
    - **ProfileScreen.kt**: Display profile data.
    - **ShopScreen.kt**: Displays items available for purchase (e.g., food, drinks, avatars).


5. **Student Help Page**

6. **Favorite Page**

---

## **Technologies Used**
- **Kotlin** (Compose Multiplatform)
- **Material3** (Modern UI components)
- **Coroutines** (Asynchronous flows using `StateFlow`)
- **Koin** (Dependency Injection)
- **Animation** (Expandable and dynamic content)

## **Setup**

This is a Kotlin Multiplatform project targeting Android, iOS, Web.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [GitHub](https://github.com/JetBrains/compose-multiplatform/issues).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.



