package com.example.proyecto01.networking.response


import com.example.proyecto01.networking.response.BlackWhite
import com.google.gson.annotations.SerializedName

data class GenerationV(
    @SerializedName("black-white")
    val blackWhite: BlackWhite
)