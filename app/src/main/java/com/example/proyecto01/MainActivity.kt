package com.example.proyecto01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.Camera
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto01.ui.theme.Proyecto01Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto01Theme {
                PokemonDetailScreen()
            }
        }
    }
}

@Composable
fun TeamScreen() {
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
@Composable
fun PokemonDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "#N Pokémon (Name)")

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = { /* Tipo 1 acción */ }) {
                Text("Tipo 1")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* Tipo 2 acción */ }) {
                Text("Tipo 2")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen del Pokémon
        Box(
            modifier = Modifier
                .size(128.dp)
                .border(1.dp, Color.Black)
        ) {
            // Imagen ficticia, reemplazar con imagen real
            Text(text = "Imagen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Evoluciones
        Column {
            Text("Debilidades:")
            Text("Resistencias:")
            Text("Inmunidades:")
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Evoluciones
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Lv7")
            Image(
                painter = painterResource(id = R.drawable.ic_camera), // Reemplazar con el recurso adecuado
                contentDescription = "Cámara",
                modifier = Modifier.size(64.dp)
            )
            Text("Lv25")
            //Icon(Icons.Default.ArrowDownward, contentDescription = "Evolución")
            Text("Lv36")
        }
    }
}
@Composable
fun UserProfileScreen() {
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
@Composable
fun CameraScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cámara",
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Icono de la cámara
        Image(
            painter = painterResource(id = R.drawable.ic_camera), // Reemplazar con el recurso adecuado
            contentDescription = "Cámara",
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Texto de guía
        Text(text = "Con la cámara")

        Spacer(modifier = Modifier.height(16.dp))

        // Contenedor para la imagen detectada (ejemplo)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(2.dp, Color.Gray)
        ) {
            Text(text = "Pokémon", modifier = Modifier.align(Alignment.Center))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable
fun PokemonListScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Nave",
            modifier = Modifier.padding(vertical = 16.dp)
        )

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
//            Spacer(modifier = Modifier.width(8.dp))
//            Icon(Icons.Default.CameraAlt, contentDescription = "Cámara")
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

@Composable
fun CustomMarginExample() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Borde superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp) // Altura mayor para el borde superior
                    .background(Color.Red) // Color del borde superior
            )

            // Borde inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(50.dp) // Altura mayor para el borde inferior
                    .background(Color.Red) // Color del borde inferior
            )

            // Bordes laterales
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp) // Ancho menor para el borde izquierdo
                    .background(Color.Red) // Color del borde izquierdo
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
                    .width(4.dp) // Ancho menor para el borde derecho
                    .background(Color.Red) // Color del borde derecho
            )

            // Contenido dentro del margen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Espacio interno
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PokemonListScreen()
            }
        }
    }
}