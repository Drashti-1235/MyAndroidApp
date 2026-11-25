
package com.example.aistudymate.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNotesClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onQuizClick: () -> Unit = {},
    onPlannerClick: () -> Unit = {},
    onProgressClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onStudyMaterialsClick: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("AIStudyMate 🎓")
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Text(
                text = "Hello, Student! 👋",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Smart Learning Partner",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "What would you like to do?",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            DashboardCard(
                title = "📚 My Notes",
                description = "Create and manage your notes",
                onClick = onNotesClick
            )

            DashboardCard(
                title = "🤖 AI Chat",
                description = "Ask your study questions",
                onClick = onChatClick
            )

            DashboardCard(
                title = "📝 Quiz",
                description = "Test your knowledge",
                onClick = onQuizClick
            )

            DashboardCard(
                title = "📖 Study Materials",
                description = "Upload and read your PDF study materials",
                onClick = onStudyMaterialsClick
            )

            DashboardCard(
                title = "📅 Study Planner",
                description = "Plan your daily study time",
                onClick = onPlannerClick
            )

            DashboardCard(
                title = "📊 Progress",
                description = "Track your learning progress",
                onClick = onProgressClick
            )

            DashboardCard(
                title = "👤 Profile",
                description = "Manage your account",
                onClick = onProfileClick
            )
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
