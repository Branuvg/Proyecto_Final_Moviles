package com.example.proyecto01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyecto01.ui.theme.Proyecto01Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto01Theme {
                //PokemonDetailScreen()
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
            }
        }
    }
}