
package com.example.aistudymate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.aistudymate.data.local.AppDatabase
import com.example.aistudymate.ui.dashboard.DashboardScreen
import com.example.aistudymate.ui.notes.NotesScreen
import com.example.aistudymate.ui.chat.ChatScreen
import com.example.aistudymate.ui.theme.AIStudyMateTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)

        setContent {
            AIStudyMateTheme {

                var currentScreen by remember {
                    mutableStateOf("dashboard")
                }

                when (currentScreen) {

                    "dashboard" -> {
                        DashboardScreen(
                            onNotesClick = {
                                currentScreen = "notes"
                            },
                            onChatClick = {
                                currentScreen = "chat"
                            }
                        )
                    }

                    "notes" -> {
                        NotesScreen(
                            noteDao = database.noteDao(),
                            onBack = {
                                currentScreen = "dashboard"
                            }
                        )
                    }

                    "chat" -> {
                        ChatScreen(
                            onBack = {
                                currentScreen = "dashboard"
                            }
                        )
                    }
                }
            }
        }
    }
}
