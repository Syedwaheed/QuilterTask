# BookListApp

A simple Android app that demonstrates the use of **MVVM architecture with Clean Code principles**,**JetPack Compose**, **RxJava**, **Retrofit**, **Hilt** and **Testing**. The app fetches book data from the Open Library API and displays it in a scrollable list with loading and error states. Users can select a book to view its details in a bottom sheet.

## Features
- Fetch book data from Open Library API using Retrofit and RxJava.
- Display books in a scrollable list with cover images, titles, and authors.
- Handle loading and error states efficiently.
- View book details in a bottom sheet upon selection.
- Implemented using MVVM architecture with Hilt for dependency injection.

## Tech Stack
- **MVVM** architecture for clean separation of concerns.
- **RxJava** for reactive programming and managing async tasks.
- **Retrofit** for API calls.
- **Hilt** for dependency injection.

## Setup
1. Clone the repository:  
   `git clone https://github.com/yourusername/BookListApp.git`
2. Open in Android Studio and sync dependencies.
3. Run the app on a physical device or emulator.

## Architecture
- **Repository**: Handles Retrofit API calls.
- **ViewModel**: Exposes data and manages UI state.
- **View**: Observes state changes (loading, success, error) and updates UI.
- **Bottom Sheet**: Displays detailed information about selected books.

## License
This project is licensed under the MIT License.
