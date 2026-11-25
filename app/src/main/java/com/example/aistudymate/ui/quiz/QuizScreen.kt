
package com.example.aistudymate.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aistudymate.data.local.QuizEntity
import com.example.aistudymate.data.local.QuizResultEntity
import com.example.aistudymate.data.repository.QuizRepository
import com.example.aistudymate.data.repository.QuizResultRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    quizRepository: QuizRepository,
    onBack: () -> Unit,
    quizResultRepository: QuizResultRepository? = null,
    onHistoryClick: () -> Unit = {}
) {
    val quizzes by quizRepository.allQuizzes.collectAsState(
        initial = emptyList()
    )

    val scope = rememberCoroutineScope()

    var section by remember { mutableStateOf("home") }

    var question by remember { mutableStateOf("") }
    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    var optionC by remember { mutableStateOf("") }
    var optionD by remember { mutableStateOf("") }
    var correctAnswer by remember { mutableStateOf("A") }

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    var resultSaved by remember { mutableStateOf(false) }

    LaunchedEffect(finished) {
        if (finished && !resultSaved && quizzes.isNotEmpty()) {
            val percentage = (score * 100) / quizzes.size

            quizResultRepository?.insertResult(
                QuizResultEntity(
                    score = score,
                    totalQuestions = quizzes.size,
                    percentage = percentage
                )
            )

            resultSaved = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AIStudyMate Quiz") },
                navigationIcon = {
                    TextButton(
                        onClick = {
                            if (section == "home") {
                                onBack()
                            } else {
                                section = "home"
                            }
                        }
                    ) {
                        Text("Back")
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

            when (section) {

                "home" -> {
                    Text(
                        "Welcome to Quiz! 🎓",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text("Total Questions: ${quizzes.size}")

                    Button(
                        onClick = { section = "add" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("➕ Add Questions")
                    }

                    Button(
                        onClick = {
                            if (quizzes.isNotEmpty()) {
                                currentIndex = 0
                                selectedAnswer = ""
                                score = 0
                                finished = false
                                resultSaved = false
                                section = "play"
                            }
                        },
                        enabled = quizzes.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("▶ Play Quiz")
                    }

                    OutlinedButton(
                        onClick = onHistoryClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📊 Quiz History")
                    }

                    if (quizzes.isEmpty()) {
                        Text("Please add questions first.")
                    }
                }

                "add" -> {
                    Text(
                        "Add New Question",
                        style = MaterialTheme.typography.titleLarge
                    )

                    OutlinedTextField(
                        value = question,
                        onValueChange = { question = it },
                        label = { Text("Question") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = optionA,
                        onValueChange = { optionA = it },
                        label = { Text("Option A") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = optionB,
                        onValueChange = { optionB = it },
                        label = { Text("Option B") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = optionC,
                        onValueChange = { optionC = it },
                        label = { Text("Option C") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = optionD,
                        onValueChange = { optionD = it },
                        label = { Text("Option D") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Select Correct Answer")

                    listOf("A", "B", "C", "D").forEach { answer ->
                        Row {
                            RadioButton(
                                selected = correctAnswer == answer,
                                onClick = { correctAnswer = answer }
                            )
                            Text(answer)
                        }
                    }

                    Button(
                        onClick = {
                            if (
                                question.isNotBlank() &&
                                optionA.isNotBlank() &&
                                optionB.isNotBlank() &&
                                optionC.isNotBlank() &&
                                optionD.isNotBlank()
                            ) {
                                scope.launch {
                                    quizRepository.insertQuiz(
                                        QuizEntity(
                                            question = question,
                                            optionA = optionA,
                                            optionB = optionB,
                                            optionC = optionC,
                                            optionD = optionD,
                                            correctAnswer = correctAnswer
                                        )
                                    )

                                    question = ""
                                    optionA = ""
                                    optionB = ""
                                    optionC = ""
                                    optionD = ""
                                    correctAnswer = "A"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Question")
                    }

                    HorizontalDivider()

                    Text("Saved Questions (${quizzes.size})")

                    quizzes.forEach { quiz ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(quiz.question)
                                Text("Correct Answer: ${quiz.correctAnswer}")

                                TextButton(
                                    onClick = {
                                        scope.launch {
                                            quizRepository.deleteQuiz(quiz)
                                        }
                                    }
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }

                "play" -> {
                    if (!finished && quizzes.isNotEmpty()) {
                        val quiz = quizzes[currentIndex]

                        Text(
                            "Question ${currentIndex + 1} of ${quizzes.size}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        LinearProgressIndicator(
                            progress = {
                                (currentIndex + 1).toFloat() / quizzes.size
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            quiz.question,
                            style = MaterialTheme.typography.titleLarge
                        )

                        val options = listOf(
                            "A" to quiz.optionA,
                            "B" to quiz.optionB,
                            "C" to quiz.optionC,
                            "D" to quiz.optionD
                        )

                        options.forEach { (key, value) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedAnswer == key,
                                    onClick = { selectedAnswer = key }
                                )

                                Text(
                                    text = "$key. $value",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (selectedAnswer.isNotEmpty()) {
                                    if (
                                        selectedAnswer.equals(
                                            quiz.correctAnswer,
                                            ignoreCase = true
                                        )
                                    ) {
                                        score++
                                    }

                                    if (currentIndex < quizzes.lastIndex) {
                                        currentIndex++
                                        selectedAnswer = ""
                                    } else {
                                        finished = true
                                    }
                                }
                            },
                            enabled = selectedAnswer.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                if (currentIndex == quizzes.lastIndex)
                                    "Finish Quiz"
                                else
                                    "Next Question"
                            )
                        }
                    }

                    if (finished) {
                        Text(
                            "Quiz Completed! 🎉",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Text(
                            "Your Score: $score / ${quizzes.size}",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            "Percentage: ${(score * 100) / quizzes.size}%"
                        )

                        Button(
                            onClick = {
                                section = "home"
                                finished = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back to Quiz Home")
                        }

                        Button(
                            onClick = {
                                currentIndex = 0
                                selectedAnswer = ""
                                score = 0
                                finished = false
                                resultSaved = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Play Again")
                        }
                    }
                }
            }
        }
    }
}
