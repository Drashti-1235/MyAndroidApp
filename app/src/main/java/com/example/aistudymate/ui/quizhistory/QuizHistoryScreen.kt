
package com.example.aistudymate.ui.quizhistory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aistudymate.data.repository.QuizResultRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizHistoryScreen(
    quizResultRepository: QuizResultRepository,
    onBack: () -> Unit
) {
    val results by quizResultRepository.allResults.collectAsState(
        initial = emptyList()
    )

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz History") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                },
                actions = {
                    if (results.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    quizResultRepository.deleteAllResults()
                                }
                            }
                        ) {
                            Text("Clear")
                        }
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                "Your Quiz Performance 📊",
                style = MaterialTheme.typography.headlineSmall
            )

            Text("Total Completed Quizzes: ${results.size}")

            if (results.isEmpty()) {

                Text("No quiz history available yet.")
                Text("Complete a quiz to see your result here.")

            } else {

                results.forEachIndexed { index, result ->

                    val date = SimpleDateFormat(
                        "dd MMM yyyy, hh:mm a",
                        Locale.getDefault()
                    ).format(Date(result.completedAt))

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Text(
                                "Quiz ${results.size - index}",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Text(
                                "Score: ${result.score} / ${result.totalQuestions}"
                            )

                            Text(
                                "Percentage: ${result.percentage}%"
                            )

                            LinearProgressIndicator(
                                progress = {
                                    result.percentage / 100f
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Text("Date: $date")
                        }
                    }
                }
            }
        }
    }
}
