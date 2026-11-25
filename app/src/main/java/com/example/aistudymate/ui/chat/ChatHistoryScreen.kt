package com.example.aistudymate.ui.chat



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aistudymate.data.local.ChatDao
import com.example.aistudymate.data.local.ChatEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHistoryScreen(
    chatDao: ChatDao,
    onBack: () -> Unit,
    onNewChat: () -> Unit,
    onOpenChat: (Int) -> Unit
) {

    var chats by remember {
        mutableStateOf<List<ChatEntity>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    val scope = rememberCoroutineScope()

    fun refreshChats() {
        scope.launch {
            chats = chatDao.getAllChats()
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        refreshChats()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat History") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                },
                actions = {
                    TextButton(onClick = onNewChat) {
                        Text("New Chat")
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
        ) {

            Button(
                onClick = onNewChat,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ New Chat")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {

                isLoading -> {
                    CircularProgressIndicator()
                }

                chats.isEmpty() -> {
                    Text("No chat history available.")
                    Text("Start a new conversation!")
                }

                else -> {

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            items = chats,
                            key = { it.id }
                        ) { chat ->

                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onOpenChat(chat.id)
                                        }
                                        .padding(16.dp)
                                ) {

                                    Text(
                                        text = chat.chatTitle,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(
                                        modifier = Modifier.height(8.dp)
                                    )

                                    Text(
                                        text = "Tap to open conversation"
                                    )

                                    Spacer(
                                        modifier = Modifier.height(8.dp)
                                    )

                                    TextButton(
                                        onClick = {
                                            scope.launch {
                                                chatDao.deleteChat(chat.id)
                                                refreshChats()
                                            }
                                        }
                                    ) {
                                        Text(
                                            "Delete Chat",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}