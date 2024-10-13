package com.example.proyecto01.ui.camara.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto01.Contorno
import com.example.proyecto01.R
import com.example.proyecto01.ui.theme.Proyecto01Theme

class Camara : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //CamaraMainApp()
        }
    }
}

@Composable
fun CamaraMainApp() {

    Contorno()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Icono de la cámara
        Image(
            painter = painterResource(id = R.drawable.ic_camera), // Reemplazar con el recurso adecuado
            contentDescription = "Cámara",
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))


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

@Preview(showBackground = true)
@Composable
fun CamaraPreview() {
    Proyecto01Theme {
        CamaraMainApp()
    }
}