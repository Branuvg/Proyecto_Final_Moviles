package com.example.proyecto01.ui.usuario.view

import android.azadevs.pokedex.Contorno
import android.azadevs.pokedex.R
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
//import com.example.proyecto01.Contorno
//import com.example.proyecto01.R
//import com.example.proyecto01.ui.theme.Proyecto01Theme
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

@Composable
fun UsuarioMainApp(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser
    var userName by remember { mutableStateOf<String?>(null) }

    // Recuperar datos del usuario desde Firestore
    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { userId ->
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val fetchedName = document.getString("username")
                    userName = fetchedName ?: "Usuario"
                    Log.d("Firestore", "Fetched username: $fetchedName")
                }
                .addOnFailureListener { e ->
                    userName = "Error"
                    Log.e("Firestore", "Error fetching user data: ${e.message}")
                }
        }
    }

    Contorno()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (currentUser != null && userName != null) {
            ProfileDesWithUser(userName ?: "Usuario", navController)
        } else {
            ProfileDes(navController)
        }
        Menu(navController)
    }
}

@Composable
fun ProfileDesWithUser(userName: String, navController: NavController) {
    val avatar = painterResource(id = R.drawable.perfil_pic)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Image(
                painter = avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Center)
                    .absoluteOffset(0.dp, 40.dp)
            )
        }

        Text(
            text = userName,
            style = TextStyle(
                fontSize = 20.sp, fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(50.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para cerrar sesión
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo("usuario") { inclusive = true } // Regresa al inicio
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                contentColor = Color.White
            )
        ) {
            Text(text = "Log Out")
        }
    }
}

@Composable
fun ProfileDes(navController: NavController) {
    val avatar = painterResource(id = R.drawable.perfil_pic)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Image(
                painter = avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Center)
                    .absoluteOffset(0.dp, 40.dp)
            )
        }

        Text(
            text = "Usuario",
            style = TextStyle(
                fontSize = 20.sp, fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(50.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de Log-in y Sign-in
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.width(120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Log-in")
            }

            Button(
                onClick = { navController.navigate("signin") },
                modifier = Modifier.width(120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Sign-in")
            }
        }
    }
}

@Composable
fun Menu(navController: NavController) { // Se agrega navController como parámetro
    val emer = painterResource(id = R.drawable.emergencia)
    val yo = painterResource(id = R.drawable.perfil)

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { navController.navigate("edit_username") }, // Navegación
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = yo,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Edit Username",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { navController.navigate("change_password") }, // Navegación
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = emer,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Change Password",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}




