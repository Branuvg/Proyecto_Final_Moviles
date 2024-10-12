package com.example.proyecto01.ui.equipo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.proyecto01.Contorno
import com.example.proyecto01.R
import com.example.proyecto01.ui.theme.Proyecto01Theme

class Equipo : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EquipoMainApp()

        }
    }
}

@Composable
fun EquipoMainApp() {
    Contorno()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barra de búsqueda
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = "", // Estado para el texto de búsqueda
                onValueChange = {},
                placeholder = { Text("Buscar") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                },
                modifier = Modifier.weight(1f)
            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Icon(Icons.Default.CameraAlt, contentDescription = "Cámara")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Tu equipo:")

        Spacer(modifier = Modifier.height(16.dp))

        // Equipo (se puede modificar para mostrar Pokémon reales)
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(6) {
                Image(
                    painter = painterResource(id = R.drawable.pokeball_icon),
                    contentDescription = "Pokeball",
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}