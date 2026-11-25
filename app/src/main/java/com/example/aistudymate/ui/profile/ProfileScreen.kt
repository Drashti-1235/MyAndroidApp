
package com.example.aistudymate.ui.profile

import android.net.Uri
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aistudymate.data.local.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: UserEntity,
    onSave: (UserEntity) -> Unit,
    onBack: () -> Unit
) {
    var name by remember(user.id) { mutableStateOf(user.name) }
    var email by remember(user.id) { mutableStateOf(user.email) }
    var photoUri by remember(user.id) { mutableStateOf(user.profileImage) }
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            photoUri = uri.toString()
        }
    }

    LaunchedEffect(photoUri) {
        bitmap = if (photoUri != null) {
            withContext(Dispatchers.IO) {
                try {
                    context.contentResolver
                        .openInputStream(Uri.parse(photoUri))
                        ?.use { BitmapFactory.decodeStream(it) }
                } catch (e: Exception) {
                    null
                }
            }
        } else null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Profile Photo",
                    modifier = Modifier.size(130.dp)
                )
            } else {
                Surface(
                    modifier = Modifier.size(130.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Profile")
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedButton(
                onClick = {
                    imagePicker.launch(
                        androidx.activity.result.PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            ) {
                Text("Select Photo from Gallery")
            }

            Spacer(modifier = Modifier.height(25.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    errorMessage = ""
                },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = ""
                },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Please enter your name"
                    } else if (!email.contains("@")) {
                        errorMessage = "Please enter a valid email"
                    } else {
                        onSave(
                            user.copy(
                                name = name.trim(),
                                email = email.trim(),
                                profileImage = photoUri
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Profile")
            }
        }
    }
}
