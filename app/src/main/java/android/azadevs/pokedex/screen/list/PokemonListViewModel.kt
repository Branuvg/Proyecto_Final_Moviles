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

                        // Fake types
                        val fakeTypes = when (number.toInt()) {
                            // Primera Generación
                            1 -> listOf("Grass", "Poison")                                                                                                                                                                                                                                  2 -> listOf("Grass", "Poison") 3 -> listOf("Grass", "Poison") 4 -> listOf("Fire") 5 -> listOf("Fire") 6 -> listOf("Fire", "Flying") 7 -> listOf("Water") 8 -> listOf("Water") 9 -> listOf("Water") 10 -> listOf("Bug") 11 -> listOf("Bug") 12 -> listOf("Bug", "Flying") 13 -> listOf("Bug", "Poison") 14 -> listOf("Bug", "Poison") 15 -> listOf("Bug", "Poison") 16 -> listOf("Normal", "Flying") 17 -> listOf("Normal", "Flying") 18 -> listOf("Normal", "Flying") 19 -> listOf("Normal") 20 -> listOf("Normal") 21 -> listOf("Normal", "Flying") 22 -> listOf("Normal", "Flying") 23 -> listOf("Poison") 24 -> listOf("Poison") 25 -> listOf("Electric") 26 -> listOf("Electric") 27 -> listOf("Ground") 28 -> listOf("Ground") 29 -> listOf("Poison") 30 -> listOf("Poison") 31 -> listOf("Poison", "Ground") 32 -> listOf("Poison") 33 -> listOf("Poison") 34 -> listOf("Poison", "Ground") 35 -> listOf("Fairy") 36 -> listOf("Fairy") 37 -> listOf("Fire") 38 -> listOf("Fire") 39 -> listOf("Normal", "Fairy") 40 -> listOf("Normal", "Fairy") 41 -> listOf("Poison", "Flying") 42 -> listOf("Poison", "Flying") 43 -> listOf("Grass", "Poison") 44 -> listOf("Grass", "Poison") 45 -> listOf("Grass", "Poison") 46 -> listOf("Bug", "Grass") 47 -> listOf("Bug", "Grass") 48 -> listOf("Bug", "Poison") 49 -> listOf("Bug", "Poison") 50 -> listOf("Ground") 51 -> listOf("Ground") 52 -> listOf("Normal") 53 -> listOf("Normal") 54 -> listOf("Water") 55 -> listOf("Water") 56 -> listOf("Fighting") 57 -> listOf("Fighting") 58 -> listOf("Fire") 59 -> listOf("Fire") 60 -> listOf("Water") 61 -> listOf("Water") 62 -> listOf("Water", "Fighting") 63 -> listOf("Psychic") 64 -> listOf("Psychic") 65 -> listOf("Psychic") 66 -> listOf("Fighting") 67 -> listOf("Fighting") 68 -> listOf("Fighting") 69 -> listOf("Grass", "Poison") 70 -> listOf("Grass", "Poison") 71 -> listOf("Grass", "Poison") 72 -> listOf("Water", "Poison") 73 -> listOf("Water", "Poison") 74 -> listOf("Rock", "Ground") 75 -> listOf("Rock", "Ground") 76 -> listOf("Rock", "Ground") 77 -> listOf("Fire") 78 -> listOf("Fire") 79 -> listOf("Water", "Psychic") 80 -> listOf("Water", "Psychic") 81 -> listOf("Electric", "Steel") 82 -> listOf("Electric", "Steel") 83 -> listOf("Normal", "Flying") 84 -> listOf("Normal", "Flying") 85 -> listOf("Normal", "Flying") 86 -> listOf("Water") 87 -> listOf("Water", "Ice") 88 -> listOf("Poison") 89 -> listOf("Poison") 90 -> listOf("Water") 91 -> listOf("Water", "Ice") 92 -> listOf("Ghost", "Poison") 93 -> listOf("Ghost", "Poison") 94 -> listOf("Ghost", "Poison") 95 -> listOf("Rock", "Ground") 96 -> listOf("Psychic") 97 -> listOf("Psychic") 98 -> listOf("Water") 99 -> listOf("Water") 100 -> listOf("Electric") 101 -> listOf("Electric") 102 -> listOf("Grass", "Psychic") 103 -> listOf("Grass", "Psychic") 104 -> listOf("Ground") 105 -> listOf("Ground") 106 -> listOf("Fighting") 107 -> listOf("Fighting") 108 -> listOf("Normal") 109 -> listOf("Poison") 110 -> listOf("Poison") 111 -> listOf("Ground", "Rock") 112 -> listOf("Ground", "Rock") 113 -> listOf("Normal") 114 -> listOf("Grass") 115 -> listOf("Normal") 116 -> listOf("Water") 117 -> listOf("Water") 118 -> listOf("Water") 119 -> listOf("Water") 120 -> listOf("Water") 121 -> listOf("Water", "Psychic") 122 -> listOf("Psychic", "Fairy") 123 -> listOf("Bug", "Flying") 124 -> listOf("Ice", "Psychic") 125 -> listOf("Electric") 126 -> listOf("Fire") 127 -> listOf("Bug") 128 -> listOf("Normal") 129 -> listOf("Water") 130 -> listOf("Water", "Flying") 131 -> listOf("Water", "Ice") 132 -> listOf("Normal") 133 -> listOf("Normal") 134 -> listOf("Water") 135 -> listOf("Electric") 136 -> listOf("Fire") 137 -> listOf("Normal") 138 -> listOf("Rock", "Water") 139 -> listOf("Rock", "Water") 140 -> listOf("Rock", "Water") 141 -> listOf("Rock", "Water") 142 -> listOf("Rock", "Flying") 143 -> listOf("Normal") 144 -> listOf("Ice", "Flying") 145 -> listOf("Electric", "Flying") 146 -> listOf("Fire", "Flying") 147 -> listOf("Dragon") 148 -> listOf("Dragon") 149 -> listOf("Dragon", "Flying") 150 -> listOf("Psychic") 151 -> listOf("Psychic")
                            152 -> listOf("Grass")                                                                                                                                                                                                                                          153 -> listOf("Grass") 154 -> listOf("Grass") 155 -> listOf("Fire") 156 -> listOf("Fire") 157 -> listOf("Fire") 158 -> listOf("Water") 159 -> listOf("Water") 160 -> listOf("Water") 161 -> listOf("Normal") 162 -> listOf("Normal") 163 -> listOf("Normal", "Flying") 164 -> listOf("Normal", "Flying") 165 -> listOf("Bug", "Flying") 166 -> listOf("Bug", "Flying") 167 -> listOf("Bug", "Poison") 168 -> listOf("Bug", "Poison") 169 -> listOf("Poison", "Flying") 170 -> listOf("Water", "Electric") 171 -> listOf("Water", "Electric") 172 -> listOf("Electric") 173 -> listOf("Fairy") 174 -> listOf("Normal", "Fairy") 175 -> listOf("Fairy") 176 -> listOf("Fairy", "Flying") 177 -> listOf("Psychic", "Flying") 178 -> listOf("Psychic", "Flying") 179 -> listOf("Electric") 180 -> listOf("Electric") 181 -> listOf("Electric") 182 -> listOf("Grass") 183 -> listOf("Water", "Fairy") 184 -> listOf("Water", "Fairy") 185 -> listOf("Rock") 186 -> listOf("Water") 187 -> listOf("Grass", "Flying") 188 -> listOf("Grass", "Flying") 189 -> listOf("Grass", "Flying") 190 -> listOf("Normal") 191 -> listOf("Grass") 192 -> listOf("Grass") 193 -> listOf("Bug", "Flying") 194 -> listOf("Water", "Ground") 195 -> listOf("Water", "Ground") 196 -> listOf("Psychic") 197 -> listOf("Dark") 198 -> listOf("Dark", "Flying") 199 -> listOf("Water", "Psychic") 200 -> listOf("Ghost") 201 -> listOf("Psychic") 202 -> listOf("Psychic") 203 -> listOf("Normal", "Psychic") 204 -> listOf("Bug") 205 -> listOf("Bug", "Steel") 206 -> listOf("Normal") 207 -> listOf("Ground", "Flying") 208 -> listOf("Steel", "Ground") 209 -> listOf("Fairy") 210 -> listOf("Fairy") 211 -> listOf("Water", "Poison") 212 -> listOf("Bug", "Steel") 213 -> listOf("Bug", "Rock") 214 -> listOf("Bug", "Fighting") 215 -> listOf("Dark", "Ice") 216 -> listOf("Normal") 217 -> listOf("Normal") 218 -> listOf("Fire") 219 -> listOf("Fire", "Rock") 220 -> listOf("Ice", "Ground") 221 -> listOf("Ice", "Ground") 222 -> listOf("Water", "Rock") 223 -> listOf("Water") 224 -> listOf("Water") 225 -> listOf("Ice", "Flying") 226 -> listOf("Water", "Flying") 227 -> listOf("Steel", "Flying") 228 -> listOf("Dark", "Fire") 229 -> listOf("Dark", "Fire") 230 -> listOf("Water", "Dragon") 231 -> listOf("Ground") 232 -> listOf("Ground") 233 -> listOf("Normal") 234 -> listOf("Normal") 235 -> listOf("Normal") 236 -> listOf("Fighting") 237 -> listOf("Fighting") 238 -> listOf("Ice", "Psychic") 239 -> listOf("Electric") 240 -> listOf("Fire") 241 -> listOf("Normal") 242 -> listOf("Normal") 243 -> listOf("Electric") 244 -> listOf("Fire") 245 -> listOf("Water") 246 -> listOf("Rock", "Ground") 247 -> listOf("Rock", "Ground") 248 -> listOf("Rock", "Dark") 249 -> listOf("Psychic", "Flying") 250 -> listOf("Fire", "Flying") 251 -> listOf("Psychic", "Grass")
                            252 -> listOf("Grass")                                                                                                                                                                                                                                          253 -> listOf("Grass") 254 -> listOf("Grass") 255 -> listOf("Fire") 256 -> listOf("Fire", "Fighting") 257 -> listOf("Fire", "Fighting") 258 -> listOf("Water") 259 -> listOf("Water", "Ground") 260 -> listOf("Water", "Ground") 261 -> listOf("Dark") 262 -> listOf("Dark") 263 -> listOf("Normal") 264 -> listOf("Normal") 265 -> listOf("Bug") 266 -> listOf("Bug") 267 -> listOf("Bug", "Flying") 268 -> listOf("Bug") 269 -> listOf("Bug", "Poison") 270 -> listOf("Water", "Grass") 271 -> listOf("Water", "Grass") 272 -> listOf("Water", "Grass") 273 -> listOf("Grass") 274 -> listOf("Grass", "Dark") 275 -> listOf("Grass", "Dark") 276 -> listOf("Normal", "Flying") 277 -> listOf("Normal", "Flying") 278 -> listOf("Water", "Flying") 279 -> listOf("Water", "Flying") 280 -> listOf("Psychic", "Fairy") 281 -> listOf("Psychic", "Fairy") 282 -> listOf("Psychic", "Fairy") 283 -> listOf("Bug", "Water") 284 -> listOf("Bug", "Flying") 285 -> listOf("Grass") 286 -> listOf("Grass", "Fighting") 287 -> listOf("Normal") 288 -> listOf("Normal") 289 -> listOf("Normal") 290 -> listOf("Bug", "Ground") 291 -> listOf("Bug", "Flying") 292 -> listOf("Bug", "Ghost") 293 -> listOf("Normal") 294 -> listOf("Normal") 295 -> listOf("Normal")


                            else -> listOf("Unknown")
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