package com.example.proyecto01.ui.usuario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto01.Contorno
import com.example.proyecto01.ui.detalle.DetalleMainApp

class Usuario : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //UsuarioMainApp()
        }
    }
}

@Composable
fun UsuarioMainApp() {

    Contorno()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Ícono del perfil
        Icon(
            Icons.Default.Person,
            contentDescription = "Perfil",
            modifier = Modifier.size(128.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Nombre de Usuario")

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Pokémon: ?/923")

        Spacer(modifier = Modifier.height(16.dp))

        // Opciones de perfil
        Button(onClick = { /* Acción cambiar contraseña */ }) {
            Text("Cambiar Contraseña")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { /* Acción cerrar sesión */ }) {
            Text("Cerrar Sesión")
        }
    }
}