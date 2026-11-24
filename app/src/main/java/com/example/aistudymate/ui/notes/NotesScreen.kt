
package com.example.aistudymate.ui.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aistudymate.data.local.NoteDao
import com.example.aistudymate.data.local.NoteEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    noteDao: NoteDao,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var editingNote by remember { mutableStateOf<NoteEntity?>(null) }

    val scope = rememberCoroutineScope()

    val notes by noteDao.getAllNotes()
        .collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Notes 📚") },
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
                text = if (editingNote == null) "Create New Note" else "Edit Note",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Note Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Note Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        scope.launch {
                            val currentNote = editingNote

                            if (currentNote == null) {
                                noteDao.insertNote(
                                    NoteEntity(
                                        title = title.trim(),
                                        content = content.trim()
                                    )
                                )
                            } else {
                                noteDao.updateNote(
                                    currentNote.copy(
                                        title = title.trim(),
                                        content = content.trim()
                                    )
                                )
                            }

                            title = ""
                            content = ""
                            editingNote = null
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text(if (editingNote == null) "Save Note" else "Update Note")
            }

            if (editingNote != null) {
                TextButton(
                    onClick = {
                        title = ""
                        content = ""
                        editingNote = null
                    }
                ) {
                    Text("Cancel Edit")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "My Saved Notes",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (notes.isEmpty()) {
                Text("No notes available. Create your first note!")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(notes, key = { it.id }) { note ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = note.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(text = note.content)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = {
                                            title = note.title
                                            content = note.content
                                            editingNote = note
                                        }
                                    ) {
                                        Text("Edit")
                                    }

                                    TextButton(
                                        onClick = {
                                            scope.launch {
                                                noteDao.deleteNote(note)

                                                if (editingNote?.id == note.id) {
                                                    title = ""
                                                    content = ""
                                                    editingNote = null
                                                }
                                            }
                                        }
                                    ) {
                                        Text("Delete")
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
