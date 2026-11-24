
package com.example.aistudymate.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aistudymate.data.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

private val httpClient = OkHttpClient()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(onBack: () -> Unit) {

    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf<ChatMessage>()
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Study Chat 🤖") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Text(
                text = "Ask your study questions!",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (messages.isEmpty()) {
                Text("No messages yet. Start your conversation!")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { chatMessage ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = if (chatMessage.isUser) {
                                    "You"
                                } else {
                                    "AI Assistant"
                                },
                                style = MaterialTheme.typography.labelLarge
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(chatMessage.message)
                        }
                    }
                }
            }

            if (isLoading) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator()
                    Text("AI is thinking...")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Type your question") },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading
                )

                Button(
                    onClick = {
                        val question = message.trim()

                        if (question.isNotEmpty() && !isLoading) {

                            messages.add(
                                ChatMessage(
                                    message = question,
                                    isUser = true
                                )
                            )

                            message = ""
                            isLoading = true

                            scope.launch {
                                try {

                                    val answer = withContext(Dispatchers.IO) {

                                        val json = JSONObject()
                                            .put("message", question)

                                        val body = json.toString()
                                            .toRequestBody(
                                                "application/json; charset=utf-8"
                                                    .toMediaType()
                                            )

                                        val request = Request.Builder()
                                            .url("http://10.0.2.2:5001/chat")
                                            .post(body)
                                            .build()

                                        httpClient.newCall(request)
                                            .execute()
                                            .use { response ->

                                                val responseText =
                                                    response.body?.string() ?: ""

                                                if (!response.isSuccessful) {
                                                    throw Exception(
                                                        JSONObject(responseText)
                                                            .optString(
                                                                "error",
                                                                "Server error"
                                                            )
                                                    )
                                                }

                                                JSONObject(responseText)
                                                    .optString(
                                                        "reply",
                                                        "AI did not return an answer."
                                                    )
                                            }
                                    }

                                    messages.add(
                                        ChatMessage(
                                            message = answer,
                                            isUser = false
                                        )
                                    )

                                } catch (e: Exception) {

                                    messages.add(
                                        ChatMessage(
                                            message = "Error: ${
                                                e.message ?: "Unable to connect to AI."
                                            }",
                                            isUser = false
                                        )
                                    )

                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    },
                    enabled = message.isNotBlank() && !isLoading
                ) {
                    Text("Send")
                }
            }
        }
    }
}
