import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit, // Callback para redirigir después de un registro exitoso
    onLoginClick: () -> Unit // Callback para navegar a la pantalla de Login
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance() // Inicializar Firestore

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sign Up",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Red,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Create an Account!", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Username Input Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Input Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Button
            Button(
                onClick = {
                    val auth = FirebaseAuth.getInstance()
                    if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val userId = auth.currentUser?.uid
                                    val user = Usuarios.User(
                                        username = username,
                                        numbers = listOf() // Lista vacía por defecto
                                    )
                                    // Guardar datos en Firestore
                                    userId?.let {
                                        firestore.collection("users").document(it).set(user)
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    context,
                                                    "Sign-up successful and data saved!",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                onSignUpSuccess() // Llama al callback de éxito
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(
                                                    context,
                                                    "Failed to save user data: ${e.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Sign-up failed: ${task.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            context,
                            "Please fill in all fields.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign Up")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Already Have an Account? Login
            TextButton(onClick = onLoginClick) {
                Text(text = "Already have an account? Login")
            }
        }
    }
}

// Clase Usuarios con la estructura requerida
class Usuarios {
    data class User(
        val username: String = "",
        val numbers: List<Int> = listOf()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserNameScreen(
    onBack: () -> Unit // Callback para regresar a la pantalla anterior
) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser
    val context = LocalContext.current

    var newUserName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Username") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Campo para cambiar el nombre de usuario
            OutlinedTextField(
                value = newUserName,
                onValueChange = { newUserName = it },
                label = { Text("New Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar cambios en el nombre de usuario
            Button(
                onClick = {
                    if (currentUser != null && newUserName.isNotEmpty()) {
                        val userId = currentUser.uid
                        firestore.collection("users").document(userId)
                            .update("username", newUserName)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Username updated successfully!", Toast.LENGTH_SHORT).show()
                                onBack()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Failed to update username: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(context, "Invalid username or user not logged in.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para limpiar el array de "numbers"
            Button(
                onClick = {
                    if (currentUser != null) {
                        val userId = currentUser.uid
                        firestore.collection("users").document(userId)
                            .update("numbers", emptyList<Int>()) // Limpia el array
                            .addOnSuccessListener {
                                Toast.makeText(context, "Array cleared successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Failed to clear array: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Clear Array", color = Color.White)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit // Callback para regresar a la pantalla anterior
) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = { Text("Current Password") },
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(
                            imageVector = if (newPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (newPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (currentPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                    val user = auth.currentUser
                    user?.let {
                        val email = it.email
                        if (email != null) {
                            val credential = EmailAuthProvider.getCredential(email, currentPassword)
                            user.reauthenticate(credential)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        user.updatePassword(newPassword)
                                            .addOnCompleteListener { updateTask ->
                                                if (updateTask.isSuccessful) {
                                                    Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                                                    onBack()
                                                } else {
                                                    Toast.makeText(context, "Failed to update password.", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                    } else {
                                        Toast.makeText(context, "Reauthentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                    }
                } else {
                    Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Change Password")
            }
        }
    }
}

