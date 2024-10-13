package com.example.proyecto01.ui.lista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto01.Contorno
import com.example.proyecto01.R

class Lista : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //ListaMainApp()
        }
    }
}

@Composable
fun ListaMainApp(navController: NavController) {

    Contorno()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra de búsqueda y botón de cámara
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = "", // Estado de búsqueda
                onValueChange = {},
                placeholder = { Text("Buscar") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.LocationOn, contentDescription = "Cámara")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de Pokémon
        LazyColumn {
            items(listOf("Poke 1", "Poke 2", "Poke 3", "Poke 4", "Poke 5", "Poke 4", "Poke 4", "Poke 4", "Poke 4", "Poke 4","Poke 4", "Poke 4", "Poke 4", "Poke 4", "Poke 4","Poke 4", "Poke 4")) { pokemon ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("detalle")
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pokeball_icon), // Icono de Pokébola
                        contentDescription = "Pokébola",
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = pokemon)
                }
            }
        }
    }
}