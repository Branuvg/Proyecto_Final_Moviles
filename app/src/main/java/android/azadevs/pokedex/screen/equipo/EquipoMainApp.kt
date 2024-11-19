package android.azadevs.pokedex.screen.equipo

import android.azadevs.pokedex.Contorno
import android.azadevs.pokedex.R
import android.azadevs.pokedex.ui.theme.PokédexTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun EquipoMainApp(navController: NavController) {
    Contorno()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(6) {
                Image(
                    painter = painterResource(id = R.drawable.pokeball_icon),
                    contentDescription = "Pokeball",
                    modifier = Modifier
                        .size(125.dp)
                        .clickable {
                            navController.navigate("detalle")
                        }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EquipoMainAppPreview() {
    val navController = rememberNavController()
    PokédexTheme {
        EquipoMainApp(navController)
    }
}