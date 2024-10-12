package com.example.proyecto01

import android.content.Intent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.proyecto01.ui.equipo.Equipo
import com.example.proyecto01.ui.equipo.EquipoMainApp
import com.example.proyecto01.ui.lista.Lista
import com.example.proyecto01.ui.theme.Proyecto01Theme
import com.example.proyecto01.ui.usuario.Usuario


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto01Theme {
                EquipoMainApp()
            }
        }
    }
}

data class BottomNavItem(
    val titulo: String,
    val iconoselec: ImageVector,
    val iconounselec: ImageVector,
    )

@Composable
fun nav() {
    val mContext = LocalContext.current
    val items = listOf(
        BottomNavItem(
            titulo = "Equipo",
            iconoselec = Icons.Filled.Home,
            iconounselec = Icons.Outlined.Home,
        ),
        BottomNavItem(
            titulo = "Buscar",
            iconoselec = Icons.Filled.Search,
            iconounselec = Icons.Outlined.Search,
        ),
        BottomNavItem(
            titulo = "Camera",
            iconoselec = Icons.Filled.Place,
            iconounselec = Icons.Outlined.Place,
        ),
        BottomNavItem(
            titulo = "Usuario",
            iconoselec = Icons.Filled.Person,
            iconounselec = Icons.Outlined.Person,
        )
    )

    var itemSelecIndex by rememberSaveable {
        mutableStateOf(0)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White // Cambiado a blanco para el fondo general
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color.Red // Fondo rojo para la barra de navegación
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = itemSelecIndex == index,
                            onClick = {
                                itemSelecIndex = index
                                //somthing
                            },
                            label = {
                                Text(
                                    text = item.titulo,
                                    color = Color.White // Texto blanco
                                )
                            },
                            icon = {
                                Icon(
                                    contentDescription = item.titulo,
                                    imageVector = if (index == itemSelecIndex) {
                                        item.iconoselec
                                    } else item.iconounselec,
                                    tint = Color.White // Iconos blancos
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                selectedTextColor = Color.White,
                                unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                indicatorColor = Color.Red.darker() // Un tono más oscuro de rojo para el indicador
                            )
                        )
                    }
                }
            }
        ) {
            // Contenido del Scaffold
        }
    }
}

// Función de extensión para obtener un color más oscuro
fun Color.darker(factor: Float = 0.1f): Color =
    Color(
        red = red * (1 - factor),
        green = green * (1 - factor),
        blue = blue * (1 - factor),
        alpha = alpha
    )


//@Composable
//fun Contorno() {
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = Color.White
//    ) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            // Borde superior
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(100.dp) // Altura mayor para el borde superior
//                    .background(Color.Red) // Color del borde superior
//            )
//
//            // Borde inferior
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .align(Alignment.BottomCenter)
//                    .height(75.dp) // Altura mayor para el borde inferior
//                    .background(Color.Red) // Color del borde inferior
//            )
//
//            // Bordes laterales
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(4.dp) // Ancho menor para el borde izquierdo
//                    .background(Color.Red) // Color del borde izquierdo
//            )
//
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .align(Alignment.CenterEnd)
//                    .width(4.dp) // Ancho menor para el borde derecho
//                    .background(Color.Red) // Color del borde derecho
//            )
//
//            // Contenido dentro del margen
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp), // Espacio interno
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//            }
//        }
//    }
//}