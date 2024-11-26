package android.azadevs.pokedex.screen.list

import android.azadevs.pokedex.Contorno
import android.azadevs.pokedex.R
import android.azadevs.pokedex.data.models.PokedexListEntry
import android.azadevs.pokedex.ui.theme.Roboto
import android.azadevs.pokedex.ui.theme.RobotoCondensed
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AddToHomeScreen
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.toBitmap
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem


@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            // Search bar para buscar por nombre o número
            SearchBar(
                hint = "Search Pokémon...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) { query ->
                viewModel.searchPokemonList(query)
            }

            // Filtro por tipo
            DropdownFilter(
                modifier = Modifier.fillMaxWidth(),
                onFilterSelected = { type ->
                    viewModel.filterPokemonByType(type) // Llama al filtro con el tipo seleccionado
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            PokemonList(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var isHintDisplayed by remember { mutableStateOf(hint.isNotEmpty()) }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 5.dp, shape = CircleShape)
                .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused && text.isEmpty()
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun DropdownFilter(
    modifier: Modifier = Modifier,
    onFilterSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("") }
    val types = listOf(
        "All", "Normal", "Fire", "Water", "Grass", "Electric", "Ice",
        "Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug",
        "Rock", "Ghost", "Dragon", "Dark", "Steel", "Fairy"
    )

    Box(modifier = modifier) {
        OutlinedTextField(
            value = if (selectedType.isEmpty()) "Select Type" else selectedType,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .clickable { expanded = true },
            label = { Text("Filter by Type") },
            trailingIcon = {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        selectedType = type
                        if (type == "All") {
                            onFilterSelected("") // Restablecer filtro
                        } else {
                            onFilterSelected(type)
                        }
                    }
                ) {
                    Text(text = type)
                }
            }
        }
    }
}




@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonState by remember {
        viewModel.pokemonListState
    }

    LazyColumn {
        val itemCount = if (pokemonState.pokemonList.size % 2 == 0) {
            pokemonState.pokemonList.size / 2
        } else {
            pokemonState.pokemonList.size / 2 + 1
        }
        items(itemCount) { index ->
            if (index >= itemCount - 1 && !pokemonState.endReached && !pokemonState.isLoading && !pokemonState.isSearching) {
                viewModel.loadPokemonPaginated()
            }
            PokedexRow(
                rowIndex = index,
                entries = pokemonState.pokemonList,
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

    val pokemonState by remember {
        viewModel.pokemonListState
    }

    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    Box(
        modifier = modifier.fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        dominantColor, defaultDominantColor
                    )
                )
            )
            .clickable {
                navController.navigate("pokemon_detail_screen/${dominantColor.toArgb()}/${entry.name}")
            },
        contentAlignment = Alignment.Center
    ) {
        Row {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(entry.imageUrl)
                    .crossfade(true)
                    .allowHardware(false)
                    .build(),
                contentDescription = entry.name,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Top)
                    .fillMaxWidth(),
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
            Text(
                text = "",
                fontSize = 0.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text(
            text = entry.name,
            fontFamily = FontFamily.Default,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Icon(imageVector = Icons.AutoMirrored.Filled.AddToHomeScreen,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(-30.dp, 0.dp)
                .size(25.dp)
                .clickable {
                    println("Pokemon Details -> Number: ${entry.number}, Name: ${entry.name}, Image URL: ${entry.imageUrl}")
                    println(pokemonState.pokemonList)
                    val hmmm = PokedexListEntry(
                        number = entry.number,
                        name = entry.name,
                        imageUrl = entry.imageUrl,
                    )
                    viewModel.addPokemonToSelected(hmmm)
                    println(pokemonState.selectedPokemonList)
                }
        )
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
                entry = entries[rowIndex], navController = navController,
                //modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}