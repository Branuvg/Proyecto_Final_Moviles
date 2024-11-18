package com.example.proyecto01.networking.response


import com.example.proyecto01.networking.response.GenerationI
import com.example.proyecto01.networking.response.GenerationIi
import com.example.proyecto01.networking.response.GenerationIii
import com.example.proyecto01.networking.response.GenerationIv
import com.example.proyecto01.networking.response.GenerationV
import com.example.proyecto01.networking.response.GenerationVi
import com.example.proyecto01.networking.response.GenerationVii
import com.example.proyecto01.networking.response.GenerationViii
import com.google.gson.annotations.SerializedName

data class Versions(
    @SerializedName("generation-i")
    val generationI: GenerationI,
    @SerializedName("generation-ii")
    val generationIi: GenerationIi,
    @SerializedName("generation-iii")
    val generationIii: GenerationIii,
    @SerializedName("generation-iv")
    val generationIv: GenerationIv,
    @SerializedName("generation-v")
    val generationV: GenerationV,
    @SerializedName("generation-vi")
    val generationVi: GenerationVi,
    @SerializedName("generation-vii")
    val generationVii: GenerationVii,
    @SerializedName("generation-viii")
    val generationViii: GenerationViii
)