package android.azadevs.pokedex

import android.azadevs.pokedex.screen.camara.CamaraMainApp
import android.azadevs.pokedex.screen.detail.PokemonDetailScreen
import android.azadevs.pokedex.screen.equipo.EquipoMainApp
import android.azadevs.pokedex.screen.list.PokemonListScreen
import android.azadevs.pokedex.screen.usuario.UsuarioMainApp
import android.azadevs.pokedex.ui.theme.PokédexTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            PokédexTheme {

                val navController = rememberNavController()
                val pantallaactual = remember { mutableStateOf("Equipo") }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = pantallaactual.value,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Red
                            )
                        )
                    },
                    bottomBar = {
                        Nav(navController,pantallaactual)
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "equipo",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("equipo") { EquipoMainApp(navController) }
                        composable(route = "pokemon_list_screen") {
                            PokemonListScreen(
                                navController = navController,
                                topPadding = paddingValues.calculateTopPadding()
                            )
                        }
                        composable("camara") {CamaraMainApp()}
                        composable("usuario") {UsuarioMainApp()}
                        composable(
                            route = "pokemon_detail_screen/{dominantColor}/{pokemonName}",
                            arguments = listOf(
                                navArgument("dominantColor") {
                                    type = NavType.IntType
                                },
                                navArgument("pokemonName") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val dominantColor = remember {
                                val color = it.arguments?.getInt("dominantColor")
                                color?.let { Color(it) } ?: Color.White
                            }
                            val pokemonName = remember {
                                it.arguments?.getString("pokemonName")
                            }
                            PokemonDetailScreen(
                                dominantColor = dominantColor,
                                pokemonName = pokemonName?.lowercase(java.util.Locale.ROOT) ?: "",
                                navController = navController,
                                topPadding = paddingValues.calculateTopPadding()
                            )
                        }
                    }
                }
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
fun Nav(navController: NavController, pantallaactual: MutableState<String>) {
    val items = listOf(
        BottomNavItem("Equipo", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("Pokemon_list_screen", Icons.Filled.Search, Icons.Outlined.Search),
        BottomNavItem("Camara", Icons.Filled.Place, Icons.Outlined.Place),
        BottomNavItem("Usuario", Icons.Filled.Person, Icons.Outlined.Person)
    )

    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    NavigationBar(
        containerColor = Color.Red
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.titulo.lowercase())
                    pantallaactual.value = item.titulo
                },
                label = {
                    Text(
                        text = item.titulo,
                        color = Color.White
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) {
                            item.iconoselec
                        } else item.iconounselec,
                        contentDescription = item.titulo,
                        tint = Color.White
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color.Red.darker()
                )
            )
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

@Composable
fun Contorno() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        //color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Bordes laterales
            Box(//izquierdo
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(Color.Red)
            )

            Box(//Derecho
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
                    .width(4.dp)
                    .background(Color.Red)
            )
        }
    }
}