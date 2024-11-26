package android.azadevs.pokedex.screen.equipo

import android.azadevs.pokedex.data.models.PokedexListEntry
import android.azadevs.pokedex.screen.list.PokemonListViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.toBitmap



@Composable
fun EquipoMainApp(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        // Cargar los últimos 6 Pokémon al iniciar la pantalla
        LaunchedEffect(Unit) {
            viewModel.loadLastSixPokemon()
        }

        Column {
            Spacer(modifier = Modifier.height(30.dp))
            PokemonList(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}


@Composable
fun PokemonList(navController: NavController,
                viewModel: PokemonListViewModel = hiltViewModel()) {

    val pokemonState by remember {
        viewModel.pokemonListState
    }


    LazyColumn {
        val itemCount = if (pokemonState.selectedPokemonList.size % 2 == 0) {
            pokemonState.selectedPokemonList.size / 2
        } else {
            pokemonState.selectedPokemonList.size / 2 + 1
        }
        items(itemCount) { index ->
            if (index >= itemCount - 1 && !pokemonState.endReached && !pokemonState.isLoading && !pokemonState.isSearching) {
                viewModel.loadPokemonPaginated()
            }
            PokedexRow(
                rowIndex = index,
                entries = pokemonState.selectedPokemonList,
                navController = navController
            )
        }
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        if (pokemonState.isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        if (pokemonState.error.isNotEmpty()) {
            RetrySection(error = pokemonState.error) {
                viewModel.loadPokemonPaginated()
            }
        }
    }
}

@Composable
fun PokedexEntry(
    entry: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {

    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    Box(
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        dominantColor, defaultDominantColor
                    )
                )
            )
            .clickable {
                navController.navigate("pokemon_detail_screen/${dominantColor.toArgb()}/${entry.name}")
                println("Pokemon Details -> Number: ${entry.number}, Name: ${entry.name}, Image URL: ${entry.imageUrl}")
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(entry.imageUrl)
                    .crossfade(true)
                    .allowHardware(false)
                    .build(),
                contentDescription = entry.name,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.scale(0.5f)
                    )
                },
                onSuccess = {
                    viewModel.calculateDominantColor(it.result.image.toBitmap()) { color ->
                        dominantColor = color
                    }
                }
            )
        }
    }
}

@Composable
fun RetrySection(error: String, onRetry: () -> Unit) {
    Column {
        Text(text = error, fontSize = 20.sp, fontFamily = FontFamily.Default)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry.invoke() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun PokedexRow(
    rowIndex: Int,
    entries: List<PokedexListEntry>,
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row {
            PokedexEntry(
                entry = entries[rowIndex * 2], navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                PokedexEntry(
                    entry = entries[rowIndex * 2 + 1], navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}

//@Composable
//fun EquipoMainApp(navController: NavController) {
//    Contorno()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(50.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
//            items(6) {
//                Image(
//                    painter = painterResource(id = R.drawable.pokeball_icon),
//                    contentDescription = "Pokeball",
//                    modifier = Modifier
//                        .size(125.dp)
//                        .clickable {
//                            navController.navigate("")
//                        }
//                )
//            }
//        }
//    }
//}