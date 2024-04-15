# UrlReader_App

# External APIs or Libraries Used

### Room Database: Utilized for local data storage.
#### Purpose: Room provides a robust and efficient way to store and access data locally on the Android device. It offers compile-time verification of SQL queries and seamless integration with LiveData and other Android Architecture Components.
#### Decision: Chosen for its simplicity, performance, and compatibility with Android development.

### Dagger Hilt: Used for dependency injection.
#### Purpose: Dagger Hilt simplifies dependency injection in Android applications by providing a set of predefined annotations and generating boilerplate code automatically.
#### Decision: Chosen for its integration with Jetpack libraries, support for Android development best practices, and reduced boilerplate code compared to manual dependency injection.

### Jetpack Compose: Utilized for building the user interface (UI).
#### Purpose: Jetpack Compose is a modern UI toolkit for Android development that enables developers to build UI components in a declarative manner.
#### Decision: Chosen for its improved developer experience, increased productivity, and flexibility compared to traditional XML-based UI development.

## Code Structure and Important Modules Overview
### App Module:
#### Purpose: Contains the main application code, including UI components, ViewModels, Dagger Hilt setup, and business logic.
### Components:
#### UI Components: Implemented using Jetpack Compose, these components represent the user interface of the application.
#### ViewModel related files: Implemented using the MVVM pattern, these classes manage UI-related logic and state.
#### Dagger Hilt Setup: Contains Dagger Hilt configuration and setup code for dependency injection.
#### Data package: Contains data-related code, such as Room database entities, DAOs, repository interfaces, and data source implementations.
#### Service package: Contains business logic regarding tracking user requests in the browser and adding those that contain “google.com” to the database.
