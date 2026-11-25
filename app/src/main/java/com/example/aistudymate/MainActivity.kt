
package com.example.aistudymate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aistudymate.data.local.AppDatabase
import com.example.aistudymate.data.local.UserEntity
import com.example.aistudymate.data.repository.AuthRepository
import com.example.aistudymate.data.repository.QuizRepository
import com.example.aistudymate.data.repository.QuizResultRepository
import com.example.aistudymate.data.repository.StudyMaterialRepository
import com.example.aistudymate.ui.auth.LoginScreen
import com.example.aistudymate.ui.auth.RegisterScreen
import com.example.aistudymate.ui.dashboard.DashboardScreen
import com.example.aistudymate.ui.notes.NotesScreen
import com.example.aistudymate.ui.chat.ChatScreen
import com.example.aistudymate.ui.chat.ChatHistoryScreen
import com.example.aistudymate.ui.profile.ProfileScreen
import com.example.aistudymate.ui.quiz.QuizScreen
import com.example.aistudymate.ui.quizhistory.QuizHistoryScreen
import com.example.aistudymate.ui.studymaterials.StudyMaterialsScreen
import com.example.aistudymate.ui.theme.AIStudyMateTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)

        val sessionPrefs = getSharedPreferences(
            "aistudymate_session",
            MODE_PRIVATE
        )

        setContent {
            AIStudyMateTheme {

                val authRepository = remember {
                    AuthRepository(database.userDao())
                }

                val quizRepository = remember {
                    QuizRepository(database.quizDao())
                }

                val quizResultRepository = remember {
                    QuizResultRepository(database.quizResultDao())
                }

                val studyMaterialRepository = remember {
                    StudyMaterialRepository(database.studyMaterialDao())
                }

                val scope = rememberCoroutineScope()

                var currentScreen by remember {
                    mutableStateOf(
                        if (sessionPrefs.getInt("user_id", -1) == -1)
                            "login"
                        else
                            "dashboard"
                    )
                }

                var selectedChatId by remember {
                    mutableStateOf<Int?>(null)
                }

                var loggedInUser by remember {
                    mutableStateOf<UserEntity?>(null)
                }

                LaunchedEffect(currentScreen) {
                    if (
                        currentScreen == "dashboard" ||
                        currentScreen == "profile"
                    ) {
                        val userId = sessionPrefs.getInt("user_id", -1)

                        if (userId != -1) {
                            loggedInUser =
                                database.userDao().getUserById(userId)
                        }
                    }
                }

                fun saveSession(user: UserEntity) {
                    sessionPrefs.edit()
                        .putInt("user_id", user.id)
                        .apply()

                    loggedInUser = user
                    currentScreen = "dashboard"
                }

                when (currentScreen) {

                    "login" -> {
                        LoginScreen(
                            authRepository = authRepository,
                            onLoginSuccess = { user ->
                                saveSession(user)
                            },
                            onRegisterClick = {
                                currentScreen = "register"
                            }
                        )
                    }

                    "register" -> {
                        RegisterScreen(
                            authRepository = authRepository,
                            onRegisterSuccess = { user ->
                                saveSession(user)
                            },
                            onLoginClick = {
                                currentScreen = "login"
                            }
                        )
                    }

                    "dashboard" -> {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {

                                TextButton(
                                    onClick = {
                                        currentScreen = "profile"
                                    }
                                ) {
                                    Text("My Profile")
                                }

                                TextButton(
                                    onClick = {
                                        sessionPrefs.edit().clear().apply()
                                        selectedChatId = null
                                        loggedInUser = null
                                        currentScreen = "login"
                                    }
                                ) {
                                    Text("Logout")
                                }
                            }

                            Box(
                                modifier = Modifier.weight(1f)
                            ) {
                                DashboardScreen(
                                    onNotesClick = {
                                        currentScreen = "notes"
                                    },
                                    onChatClick = {
                                        currentScreen = "chatHistory"
                                    },
                                    onQuizClick = {
                                        currentScreen = "quiz"
                                    },
                                    onStudyMaterialsClick = {
                                        currentScreen = "studyMaterials"
                                    }
                                )
                            }
                        }
                    }

                    "profile" -> {
                        val user = loggedInUser

                        if (user != null) {
                            ProfileScreen(
                                user = user,
                                onSave = { updatedUser ->
                                    scope.launch {
                                        database.userDao()
                                            .updateUser(updatedUser)

                                        loggedInUser = updatedUser
                                        currentScreen = "dashboard"
                                    }
                                },
                                onBack = {
                                    currentScreen = "dashboard"
                                }
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    "notes" -> {
                        NotesScreen(
                            noteDao = database.noteDao(),
                            onBack = {
                                currentScreen = "dashboard"
                            }
                        )
                    }

                    "quiz" -> {
                        QuizScreen(
                            quizRepository = quizRepository,
                            quizResultRepository = quizResultRepository,
                            onBack = {
                                currentScreen = "dashboard"
                            },
                            onHistoryClick = {
                                currentScreen = "quizHistory"
                            }
                        )
                    }

                    "quizHistory" -> {
                        QuizHistoryScreen(
                            quizResultRepository = quizResultRepository,
                            onBack = {
                                currentScreen = "quiz"
                            }
                        )
                    }

                    "studyMaterials" -> {
                        StudyMaterialsScreen(
                            studyMaterialRepository = studyMaterialRepository,
                            onBack = {
                                currentScreen = "dashboard"
                            }
                        )
                    }

                    "chatHistory" -> {
                        ChatHistoryScreen(
                            chatDao = database.chatDao(),
                            onBack = {
                                currentScreen = "dashboard"
                            },
                            onNewChat = {
                                selectedChatId = null
                                currentScreen = "chat"
                            },
                            onOpenChat = { id ->
                                selectedChatId = id
                                currentScreen = "chat"
                            }
                        )
                    }

                    "chat" -> {
                        ChatScreen(
                            chatDao = database.chatDao(),
                            onBack = {
                                currentScreen = "chatHistory"
                            },
                            existingChatId = selectedChatId
                        )
                    }
                }
            }
        }
    }
}
