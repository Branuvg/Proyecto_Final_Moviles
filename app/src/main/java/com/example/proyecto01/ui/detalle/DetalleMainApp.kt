package com.example.proyecto01.ui.detalle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.proyecto01.R
import com.example.proyecto01.ui.equipo.EquipoMainApp

class Detalle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DetalleMainApp()
        }
    }
}

@Composable
fun DetalleMainApp() {
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