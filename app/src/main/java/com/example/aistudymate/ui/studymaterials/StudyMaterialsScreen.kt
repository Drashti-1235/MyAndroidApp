package com.example.aistudymate.ui.studymaterials




import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aistudymate.data.local.StudyMaterialEntity
import com.example.aistudymate.data.repository.StudyMaterialRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyMaterialsScreen(
    studyMaterialRepository: StudyMaterialRepository,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var subject by remember { mutableStateOf("") }

    val materials by studyMaterialRepository.allMaterials
        .collectAsState(initial = emptyList())

    val pdfPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->

        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
                // Continue if permission is already available.
            }

            var fileName = "Study Material.pdf"

            val cursor = context.contentResolver.query(
                uri,
                null,
                null,
                null,
                null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(
                        OpenableColumns.DISPLAY_NAME
                    )
                    if (nameIndex >= 0) {
                        fileName = it.getString(nameIndex)
                    }
                }
            }

            scope.launch {
                studyMaterialRepository.insertMaterial(
                    StudyMaterialEntity(
                        title = fileName,
                        subject = subject.trim(),
                        pdfUri = uri.toString()
                    )
                )
                subject = ""
                Toast.makeText(
                    context,
                    "PDF added successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Materials") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
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
        ) {

            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Enter Subject Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    pdfPicker.launch(arrayOf("application/pdf"))
                },
                enabled = subject.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload PDF")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "My Study Materials",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (materials.isEmpty()) {
                Text("No study materials added yet.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = materials,
                        key = { it.id }
                    ) { material ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = material.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "Subject: ${material.subject}"
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            try {
                                                val intent = Intent(
                                                    Intent.ACTION_VIEW
                                                ).apply {
                                                    setDataAndType(
                                                        Uri.parse(material.pdfUri),
                                                        "application/pdf"
                                                    )
                                                    addFlags(
                                                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                                    )
                                                }
                                                context.startActivity(intent)
                                            } catch (_: Exception) {
                                                Toast.makeText(
                                                    context,
                                                    "No PDF viewer found",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    ) {
                                        Text("Open PDF")
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            scope.launch {
                                                studyMaterialRepository
                                                    .deleteMaterial(material)
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
