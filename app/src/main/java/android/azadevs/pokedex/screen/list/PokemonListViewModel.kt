package android.azadevs.pokedex.screen.list

import android.azadevs.pokedex.data.models.PokedexListEntry
import android.azadevs.pokedex.data.repository.PokemonRepository
import android.azadevs.pokedex.util.Constants.PAGE_SIZE
import android.azadevs.pokedex.util.Resource
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var currentPage = 0

    var pokemonListState = mutableStateOf(PokemonListState())

    private var cachedPokemonList = listOf<PokedexListEntry>()
    private var isSearchStarting = true

    init {
        loadPokemonPaginated()
    }

    fun addPokemonToSelected(entry: PokedexListEntry) {
        val updatedList = pokemonListState.value.selectedPokemonList.toMutableList()
        if (!updatedList.contains(entry)) {
            updatedList.add(entry)
            pokemonListState.value = pokemonListState.value.copy(selectedPokemonList = updatedList)

            // Actualizar Firestore
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            val currentUser = auth.currentUser

            currentUser?.uid?.let { userId ->
                firestore.collection("users").document(userId)
                    .update("numbers", FieldValue.arrayUnion(entry.number)) // Suponiendo que `entry.id` es el identificador que quieres guardar
                    .addOnSuccessListener {
                        Log.d("Firestore", "Added ${entry.number} to numbers array")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Failed to add ${entry.number} to numbers array: ${e.message}")
                    }
            }
        }
    }

    fun removePokemonFromSelected(entry: PokedexListEntry) {
        val updatedList = pokemonListState.value.selectedPokemonList.toMutableList()
        updatedList.remove(entry)
        pokemonListState.value = pokemonListState.value.copy(selectedPokemonList = updatedList)

        // Actualizar Firestore
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        currentUser?.uid?.let { userId ->
            firestore.collection("users").document(userId)
                .update("numbers", FieldValue.arrayRemove(entry.number)) // Suponiendo que `entry.id` es el identificador que quieres eliminar
                .addOnSuccessListener {
                    Log.d("Firestore", "Removed ${entry.number} from numbers array")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Failed to remove ${entry.number} from numbers array: ${e.message}")
                }
        }
    }

    fun loadLastSixPokemon() {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        currentUser?.uid?.let { userId ->
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val numbers = document.get("numbers") as? List<Int> ?: listOf()
                    val lastSix = numbers.takeLast(6) // Obtener los últimos 6 números
                    fetchPokemonDetails(lastSix) // Convertir números a objetos PokedexListEntry
                }
                .addOnFailureListener { e ->
                    pokemonListState.value = pokemonListState.value.copy(error = e.message ?: "Unknown error")
                }
        }
    }

    private fun fetchPokemonDetails(ids: List<Int>) {
        viewModelScope.launch {
            try {
                val fetchedPokemon = ids.map { id ->
                    // Crear objetos PokedexListEntry usando solo el número
                    PokedexListEntry(
                        number = id,
                        name = cachedPokemonList[id-1].name, // Puedes reemplazar esto con un nombre real si tienes una API
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
                    )
                }
                pokemonListState.value = pokemonListState.value.copy(selectedPokemonList = fetchedPokemon)
            } catch (e: Exception) {
                pokemonListState.value = pokemonListState.value.copy(error = e.message ?: "Unknown error")
            }
        }
    }


    fun searchPokemonList(query: String) {
        val listToSearch = if (isSearchStarting) {
            pokemonListState.value.pokemonList
        } else {
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemonListState.value = pokemonListState.value.copy(
                    pokemonList = cachedPokemonList,
                    isSearching = false
                )
                isSearchStarting = true
                return@launch
            }
            val result = listToSearch.filter {
                it.name.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if (isSearchStarting) {
                cachedPokemonList = pokemonListState.value.pokemonList
                isSearchStarting = false
            }
            pokemonListState.value =
                pokemonListState.value.copy(pokemonList = result, isSearching = true)
        }
    }

    fun filterPokemonByType(type: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (type.isEmpty() || type == "All") {
                // Restaurar la lista completa si el filtro es vacío o "All"
                pokemonListState.value = pokemonListState.value.copy(
                    pokemonList = cachedPokemonList,
                    isSearching = false
                )
                return@launch
            }

            // Filtrar los Pokémon que contienen el tipo especificado
            val filteredList = cachedPokemonList.filter { entry ->
                entry.type.any { it.equals(type, ignoreCase = true) }
            }

            // Actualizar el estado con la lista filtrada
            pokemonListState.value = pokemonListState.value.copy(
                pokemonList = filteredList,
                isSearching = true
            )
        }
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            pokemonListState.value = pokemonListState.value.copy(isLoading = true)
            val result =
                repository.getPokemonList(
                    limit = PAGE_SIZE,
                    offset = currentPage * PAGE_SIZE
                )
            when (result) {
                is Resource.Error -> {
                    pokemonListState.value =
                        pokemonListState.value.copy(error = result.message!!, isLoading = false)
                }

                is Resource.Success -> {
                    val pokedexEntries = result.data!!.results.map { data ->
                        val number = if (data.url.endsWith("/")) {
                            data.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            data.url.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"

                        // Fake types asignados manualmente, incluyendo Pokémon con doble tipo
                        val fakeTypes = when (number.toInt()) {
                            // Normal
                            in listOf(16, 19, 52, 83, 84, 108, 113, 115, 128, 132) -> listOf("Normal")

                            // Fire
                            in listOf(4, 37, 58, 77, 126, 136, 155, 218, 228) -> listOf("Fire")

                            // Water
                            in listOf(7, 54, 60, 72, 79, 86, 90, 98, 116, 118) -> listOf("Water")

                            // Grass
                            in listOf(1, 43, 69, 102, 114, 152, 187, 188, 191, 252) -> listOf("Grass")

                            // Electric
                            in listOf(25, 81, 100, 125, 135, 145, 179, 180, 181, 239) -> listOf("Electric")

                            // Ice
                            in listOf(87, 91, 124, 131, 144, 215, 220, 221, 225, 361) -> listOf("Ice")

                            // Fighting
                            in listOf(56, 57, 66, 67, 68, 106, 107, 214, 236, 237) -> listOf("Fighting")

                            // Poison
                            in listOf(23, 24, 29, 32, 41, 88, 109, 48, 72, 316) -> listOf("Poison")

                            // Ground
                            in listOf(50, 51, 27, 28, 104, 105, 231, 232, 194, 195) -> listOf("Ground")

                            // Flying
                            in listOf(16, 21, 41, 83, 84, 123, 130, 142, 144, 145) -> listOf("Flying")

                            // Pokémon con doble tipo
                            6 -> listOf("Fire", "Flying")     // Charizard
                            3 -> listOf("Grass", "Poison")   // Venusaur
                            9 -> listOf("Water", "Fighting") // Blastoise
                            12 -> listOf("Bug", "Flying")    // Butterfree
                            18 -> listOf("Normal", "Flying") // Pidgeot
                            59 -> listOf("Fire", "Rock")     // Arcanine
                            65 -> listOf("Psychic", "Fighting") // Alakazam (supuesto ejemplo)
                            94 -> listOf("Ghost", "Poison")  // Gengar
                            149 -> listOf("Dragon", "Flying") // Dragonite
                            143 -> listOf("Normal", "Ground") // Snorlax (supuesto ejemplo)

                            else -> listOf("Unknown") // Por defecto
                        }

                        PokedexListEntry(
                            number = number.toInt(),
                            name = data.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                            },
                            imageUrl = url,
                            type = fakeTypes // Asignar tipos dobles si aplica
                        )
                    }



                    // Actualizar las listas
                    cachedPokemonList = cachedPokemonList + pokedexEntries
                    pokemonListState.value = pokemonListState.value.copy(
                        pokemonList = cachedPokemonList,
                        isLoading = false,
                        error = ""
                    )
                    currentPage++
                }
            }
        }
    }







    fun calculateDominantColor(bitmap: Bitmap, onFinish: (Color) -> Unit) {
        val bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}

data class PokemonListState(
    val isLoading: Boolean = false,
    val error: String = "",
    val pokemonList: List<PokedexListEntry> = emptyList(),
    val endReached: Boolean = false,
    val isSearching: Boolean = false,
    val selectedPokemonList: List<PokedexListEntry> = emptyList()
)