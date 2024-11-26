package android.azadevs.pokedex.data.models

data class PokedexListEntry(
    val number: Int,
    val name: String,
    val imageUrl: String,
    val type: List<String> = emptyList() // Add a list of types for filtering
)

