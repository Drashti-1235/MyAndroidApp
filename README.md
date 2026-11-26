# AIStudyMate – AI-Based Student Study Assistant

AIStudyMate is an Android application designed to help students manage their study activities in one place. It provides notes management, AI chat assistance, quiz creation and playing, quiz history, profile management, and PDF study materials.

## Features

- User Login and Registration
- Dashboard
- Profile Management
- My Notes – Add, Edit and Delete notes
- AI Chat Assistant
- Chat History
- Quiz Creation and Playing
- Quiz History and Scores
- Study Materials – PDF selection, opening and deleting
- Local data storage using Room Database

## Technologies Used

- Kotlin
- Android Studio
- Jetpack Compose
- Material Design 3
- Room Database
- Node.js
- Express.js
- Gemini AI
- Git and GitHub

## Project Structure

- `app/` – Android application module
- `data/local/` – Room database, entities and DAO
- `data/repository/` – Repository classes for data management
- `ui/` – Application user interface and screens
- `MainActivity.kt` – Main navigation and application entry point

## Application Modules

### Authentication
Login and registration screens for users.

### Dashboard
Main screen to access different application features.

### My Notes
Create, edit and delete study notes.

### AI Chat
Chat interface designed to connect with Gemini AI through the backend.

### Chat History
Access saved chat history.

### Quiz
Create questions, play quizzes and calculate scores.

### Quiz History
View previous quiz attempts and performance.

### Profile
Manage profile information and profile picture.

### Study Materials
Select PDF documents, display saved materials, open PDFs and delete entries from the list.

## Database

Room Database is used for local data storage. Database entities, DAO interfaces and repositories manage notes, quizzes, quiz results and study materials.

## Backend

The backend is developed using Node.js and Express.js for AI chat integration with Gemini AI.

## Future Scope

- Study Planner
- Study Progress Tracking
- Notifications and Reminders
- Improved AI Chat
- Cloud Data Synchronization
- Personalized Study Recommendations

## Repository

GitHub Repository:  
https://github.com/Drashti-1235/MyAndroidApp

## Developer

Developed as an MSc IT Android application project.
